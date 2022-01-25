package nextstep.subway.acceptance.stationstep;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationValidateStep {

    public static void 역_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus created) {
        assertThat(response.statusCode()).isEqualTo(created.value());
    }

    public static void 역_응답_바디_검증(ExtractableResponse<Response> response) {
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 역_목록_조회_바디_검증(String 강남역, String 역삼역, ExtractableResponse<Response> response) {
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).contains(강남역, 역삼역);
    }

    public static void 역_응답_바디_개수_검증(ExtractableResponse<Response> stationList, int count) {
        assertThat(stationList.body().jsonPath().getList("name").size()).isEqualTo(count);
    }
}
