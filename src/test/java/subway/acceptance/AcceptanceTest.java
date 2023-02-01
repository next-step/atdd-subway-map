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
    public static final String STATION_DELETE_URL = STATION_BASE_URL + "/{stationId}";

    public static final String LINE_BASE_URL = "/lines";
    public static final String LINE_SELECT_URL = LINE_BASE_URL + "/{lineId}";
    public static final String LINE_UPDATE_URL = LINE_BASE_URL + "/{lineId}";
    public static final String LINE_DELETE_URL = LINE_BASE_URL + "/{lineId}";

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
                .when().delete(STATION_DELETE_URL, stationId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> responseBody) {
        return given().log().all()
                .body(responseBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return given().log().all()
                .when().get(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_단건_조회_요청(String lineId) {
        return given().log().all()
                .when().get(LINE_SELECT_URL, lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String lineId, Map<String, String> requestBody) {
        return given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(LINE_UPDATE_URL, lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String lineId) {
        return given().log().all()
                .when().delete(LINE_DELETE_URL, lineId)
                .then().log().all()
                .extract();
    }
}
