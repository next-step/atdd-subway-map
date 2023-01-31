package subway.common.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class CommonApi {

    public static final String LOCATION = "Location";

    public static ExtractableResponse<Response> showResource(ExtractableResponse<Response> response) {
        String location = response.header(LOCATION);
        return given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get(location)
                .then()
                    .log().all()
                    .extract();
    }
}
