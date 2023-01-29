package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AcceptanceTest {

    public static final String STATION_BASE_URL = "/stations";
    public static final String DELETE_STATION_URL = STATION_BASE_URL + "/{stationId}";

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> requestBody) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return given().log().all()
                .when().get(STATION_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static void 지하철역_삭제_요청(Long stationId) {
        given().log().all()
                .when().delete(DELETE_STATION_URL, stationId)
                .then().log().all()
                .extract();
    }
}
