package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.LineApiRequest.경강선_생성;
import static subway.util.LineApiRequest.신분당선_색상;
import static subway.util.LineApiRequest.신분당선_생성;
import static subway.util.LineApiRequest.신분당선_이름;
import static subway.util.LineApiRequest.지하철_노선_리스폰_변환;
import static subway.util.LineApiRequest.지하철_노선_목록_조회_요청;
import static subway.util.LineApiRequest.지하철_노선_생성_요청;
import static subway.util.LineApiRequest.지하철_노선_조회_요청;
import static subway.util.StationApiRequest.강남역;
import static subway.util.StationApiRequest.양재역;
import static subway.util.StationApiRequest.지하철역_리스폰_변환;
import static subway.util.StationApiRequest.지하철역_생성_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(강남역));
        StationResponse downStation = 지하철역_리스폰_변환(지하철역_생성_요청(양재역));

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_이름, 신분당선_색상,
            upStation.getId(), downStation.getId(), 10);

        // then
        지하철_노선_생성됨(response);
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철_노선_목록_조회됨(지하철_노선_목록_조회_요청(), 지하철_노선_리스폰_변환(response));
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());
        LineResponse 경강선 = 지하철_노선_리스폰_변환(경강선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회됨(response, 신분당선, 경강선);
    }

    private void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, LineResponse ... lineResponses) {
        List<LineResponse> responses = response.jsonPath().getList(".", LineResponse.class);

        IntStream.range(0, responses.size())
            .forEach(i -> 지하철_노선_조회됨(response, responses.get(i), lineResponses[i]));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선.getId());

        // then
        지하철_노선_조회됨(response, 지하철_노선_리스폰_변환(response), 신분당선);
    }

    private void 지하철_노선_조회됨(ExtractableResponse<Response> response, LineResponse actual, LineResponse expected) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getColor()).isEqualTo(expected.getColor());

        List<StationResponse> actualStations = actual.getStations();
        List<StationResponse> expectedStations = expected.getStations();

        IntStream.range(0, actualStations.size())
            .forEach(i -> {
                assertThat(actualStations.get(i).getId()).isEqualTo(expectedStations.get(i).getId());
                assertThat(actualStations.get(i).getName()).isEqualTo(expectedStations.get(i).getName());
            });
    }
}
