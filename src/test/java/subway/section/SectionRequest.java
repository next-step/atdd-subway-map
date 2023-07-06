package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionRequest {
    private SectionRequest() {
    }

    public static void 지하철_구간을_등록한다() {
        Map<String, ? extends Number> params = Map.of(
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 10
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", lineId)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }
}
