package subway.section.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionAssuredFixture {

    public static void 예외_검증(ExtractableResponse<Response> response, String message){
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("message")).isEqualTo(message);
    }

    public static void 삭제된_구간_검증(ExtractableResponse<Response> response){
        final String message = "해당 엔티티가 존재하지 않습니다";
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(response.jsonPath().getString("message")).isEqualTo(message);
    }

    public static void 지하철구간_검증(ExtractableResponse<Response> getResponse, Long upStationId, Long downStationId) {
        assertThat(getResponse.jsonPath().getLong("downStationId")).isEqualTo(downStationId);
        assertThat(getResponse.jsonPath().getLong("upStationId")).isEqualTo(upStationId);
    }
}
