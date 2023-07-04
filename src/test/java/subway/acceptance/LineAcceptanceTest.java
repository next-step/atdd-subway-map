package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.LineApiRequest.경강선_색상;
import static subway.util.LineApiRequest.경강선_생성;
import static subway.util.LineApiRequest.경강선_이름;
import static subway.util.LineApiRequest.신분당선_색상;
import static subway.util.LineApiRequest.신분당선_생성;
import static subway.util.LineApiRequest.신분당선_이름;
import static subway.util.LineApiRequest.지하철_노선_리스폰_변환;
import static subway.util.LineApiRequest.지하철_노선_목록_조회_요청;
import static subway.util.LineApiRequest.지하철_노선_생성_요청;
import static subway.util.LineApiRequest.지하철_노선_수정_요청;
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
import subway.exception.LineDuplicationNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

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

    @DisplayName("지하철 노선을 생성할 때 중복된 이름이 있으면 실패한다.")
    @Test
    void crateLineFail() {
        // given
        신분당선_생성();

        // when
        ExtractableResponse<Response> response = 신분당선_생성();

        // then
        요청_실패됨(response, new LineDuplicationNameException());
    }

    @DisplayName("지하철 노선을 생성할 때 해당 지하철역이 없으면 실패한다.")
    @Test
    void crateLineFail2() {
        // given
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(강남역));

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_이름, 신분당선_색상,
            upStation.getId(), 2L, 10);

        // then
        요청_실패됨(response, new StationNotFoundException());
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

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("지하철 노선을 조회할 때 해당 지하철 노선이 없으면 실패한다.")
    @Test
    void showLineFail() {
        // given

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(1L);

        // then
        요청_실패됨(response, new LineNotFoundException());
    }
    
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(신분당선.getId(), 경강선_이름, 경강선_색상);

        // then
        LineResponse expected = new LineResponse(신분당선.getId(), 경강선_이름, 경강선_색상, 신분당선.getStations());
        지하철_노선_수정됨(response, expected);
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response, LineResponse expected) {
        ExtractableResponse<Response> findResponse = 지하철_노선_조회_요청(expected.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        지하철_노선_조회됨(findResponse, 지하철_노선_리스폰_변환(findResponse), expected);
    }

    @DisplayName("지하철 노선을 수정할 때 해당 지하철 노선이 없으면 실패한다.")
    @Test
    void updateLineFail() {
        // given

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(1L, 경강선_이름, 경강선_색상);

        // then
        요청_실패됨(response, new LineNotFoundException());
    }
}
