package subway.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AcceptanceTestUtil {

    public static <T> ExtractableResponse<Response> create(String path, T request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }
}
