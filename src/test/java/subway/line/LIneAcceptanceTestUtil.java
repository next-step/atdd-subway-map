package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LIneAcceptanceTestUtil {
    private LIneAcceptanceTestUtil() {
    }

    public static Long 노선이_생성되어_있다(final String name, final String color, final Long upStationId, final Long downStationId) {
        Map<String, String> params1 = createLineRequestPixture(name, color, upStationId, downStationId);
        final LineResponse lineResponse = apiCreateLine(params1).as(LineResponse.class);
        return lineResponse.getId();
    }

    public static ExtractableResponse<Response> apiCreateLine(final Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static Map<String, String> createLineRequestPixture(final String name, final String color, final Long upStationId, final Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        return params;
    }
}
