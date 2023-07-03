package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.LineApiRequest.경강선_생성;
import static subway.util.LineApiRequest.신분당선_색상;
import static subway.util.LineApiRequest.신분당선_생성;
import static subway.util.LineApiRequest.신분당선_이름;
import static subway.util.LineApiRequest.지하철_노선_리스폰_변환;
import static subway.util.LineApiRequest.지하철_노선_목록_조회_요청;
import static subway.util.LineApiRequest.지하철_노선_생성_요청;
import static subway.util.StationApiRequest.강남역;
import static subway.util.StationApiRequest.양재역;
import static subway.util.StationApiRequest.지하철역_리스폰_변환;
import static subway.util.StationApiRequest.지하철역_생성_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
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
        지하철_노선_생성됨(response, 신분당선_이름, 신분당선_색상, upStation.getName(), downStation.getName());
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response, String name, String color,
        String ... stationNames) {
        List<String> names = response.jsonPath().getList("stations.name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getLong("id")).isEqualTo(1L);
        assertThat(response.jsonPath().getString("name")).isEqualTo(name);
        assertThat(response.jsonPath().getString("color")).isEqualTo(color);
        assertThat(names.size()).isEqualTo(2);
        assertThat(names).containsAnyOf(stationNames);
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
        List<String> names = response.jsonPath().getList("name", String.class);
        List<String> colors = response.jsonPath().getList("color", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(names).containsAnyOf(Arrays.stream(lineResponses)
            .map(LineResponse::getName)
            .toArray(String[]::new));
        assertThat(colors).containsAnyOf(Arrays.stream(lineResponses)
            .map(LineResponse::getColor)
            .toArray(String[]::new));
        containsStationNames(response, lineResponses);
    }

    private void containsStationNames(ExtractableResponse<Response> response, LineResponse ... lineResponses) {
        for (int i = 0; i < lineResponses.length; i++) {
            List<String> lineStationNames = response.jsonPath()
                .getList(String.format("[%d].stations.name", i), String.class);
            assertThat(lineStationNames).containsAnyOf(lineResponses[i].getStations()
                .stream()
                .map(StationResponse::getName)
                .toArray(String[]::new));
        }
    }
}
