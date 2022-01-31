package nextstep.subway.acceptance.stationstep;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationValidateStep {

    public static void 역_생성_응답_검증(ExtractableResponse<Response> response, HttpStatus status) {
        역_응답_상태_검증(response, status);
        역_생성_응답_헤더_검증(response, "Location");
    }

    public static void 역_목록_조회_응답_검증(ExtractableResponse<Response> response, HttpStatus status,
                                     String 역1, String 역2) {
        역_응답_상태_검증(response, status);
        역_목록_조회_바디_검증(역1, 역2, response);
    }

    public static void 역_삭제_응답_검증(ExtractableResponse<Response> response, HttpStatus status) {
        역_응답_상태_검증(response, status);
    }

    public static void 역_이름_중복_응답_검증(ExtractableResponse<Response> response, HttpStatus status) {
        역_응답_상태_검증(response, status);
    }

    private static void 역_이름_중복_바디_검증(ExtractableResponse<Response> response, String station) {
        assertThat(response.body().jsonPath().getList("name")).containsExactly(station);
    }

    private static void 역_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private static void 역_생성_응답_헤더_검증(ExtractableResponse<Response> response, String header) {
        assertThat(response.header(header)).isNotBlank();
    }

    private static void 역_목록_조회_바디_검증(String 강남역, String 역삼역, ExtractableResponse<Response> response) {
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
    }
}
