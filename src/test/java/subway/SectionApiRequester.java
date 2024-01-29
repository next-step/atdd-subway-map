package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SectionApiRequester {

    public static ExtractableResponse<Response> addSectionToLine(
        final long lineId,
        final Map<String,? extends Number> body
    ) {
        return RestAssured.given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("lineId", lineId)
            .when().post("/lines/{lineId}/sections")
            .then().log().all()
            .extract();
    }
}
