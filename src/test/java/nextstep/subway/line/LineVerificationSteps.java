package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineVerificationSteps {

    public static void 지하철_노선_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        생성된_지하철_노선_URI_경로_존재_함(response);
    }

    public static void 지하철_노선_생성_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 생성된_지하철_노선_URI_경로_존재_함(ExtractableResponse<Response> response) {
        assertThat(생성된_지하철_노선의_URI_경로(response)).isNotBlank();
    }

    public static String 생성된_지하철_노선의_URI_경로(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

    public static void 지하철_노선_목록_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_조회_결과에_생성된_노선_포함_확인(ExtractableResponse<Response> readLinesResponse) {
        List<LineResponse> lines = readLinesResponse.jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(2);
        지하철_노선_목록_조회_결과에_생성된_노선_역이름_확인(lines, 0, "강남역", "양재역");
        지하철_노선_목록_조회_결과에_생성된_노선_역이름_확인(lines, 1, "강남역", "역삼역");
    }

    private static void 지하철_노선_목록_조회_결과에_생성된_노선_역이름_확인(List<LineResponse> lines, int index, String upStationName, String downStationName) {
        List<StationResponse> stationResponses = lines.get(index).getStations();
        List<String> collect = stationResponses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(collect).containsExactly(upStationName, downStationName);
    }

    public static void 지하철_노선_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_수정_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_수정_실패_됨(ExtractableResponse<Response> updateResponse) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 지하철_노선_제거_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_제거_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
