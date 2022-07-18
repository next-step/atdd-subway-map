package nextstep.subway.validate;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpStatusValidate {

    public static void 상태코드_체크(ExtractableResponse<Response> response,
                                          HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static void 지하철역이_존재하는지_체크(List<String> stationList, String name) {
        assertThat(stationList).containsExactly(name);
    }

    public static void 목록개수_확인(List<String> stationList, int size) {
        assertThat(stationList).hasSize(size);
    }

    public static void 값이_포함되지_않는지_검증(List<String> stationList, String name) {
        assertThat(stationList).doesNotContain(name);
    }

    public static void 지하철_노선_사이즈_체크(List<String> stationLineList, int size) {
        assertThat(stationLineList).hasSize(size);
    }

    public static void 동일한_값_인지_검증(String stationName, String name) {
        assertThat(stationName).isEqualTo(name);
    }

    public static void 에러메시지_체크(ExtractableResponse<Response> response, String message) {
        assertThat(response.jsonPath().getString("message")).isEqualTo(message);
    }

}
