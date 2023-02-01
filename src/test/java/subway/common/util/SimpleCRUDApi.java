package subway.common.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class SimpleCRUDApi {

    private SimpleCRUDApi() {}

    public static final String LOCATION = "Location";

    /**
     * @param response 조회할 Resource의 location 정보가 기입된 response를 입력해야한다.
     * @return
     */
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
