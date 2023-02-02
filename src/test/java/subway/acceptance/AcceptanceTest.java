package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subway.common.util.DatabaseCleaner;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static subway.common.util.RestAssuredBuilder.기본_헤더값_설정;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("acceptanceTest")
class AcceptanceTest {

    public static final String STATION_BASE_URL = "/stations";
    public static final String STATION_COMMAND_URL = STATION_BASE_URL + "/{stationId}";

    public static final String LINE_BASE_URL = "/lines";
    public static final String LINE_COMMAND_URL = LINE_BASE_URL + "/{lineId}";

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanUp() {
        databaseCleaner.execute();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> requestBody) {
        return given(기본_헤더값_설정()).log().all()
                .body(requestBody)
                .when().post(STATION_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return given(기본_헤더값_설정()).log().all()
                .when().get(STATION_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static void 지하철역_삭제_요청(String stationId) {
        given(기본_헤더값_설정()).log().all()
                .when().delete(STATION_COMMAND_URL, stationId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> responseBody) {
        return given(기본_헤더값_설정()).log().all()
                .body(responseBody)
                .when().post(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return given(기본_헤더값_설정()).log().all()
                .when().get(LINE_BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_단건_조회_요청(String lineId) {
        return given(기본_헤더값_설정()).log().all()
                .when().get(LINE_COMMAND_URL, lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String lineId, Map<String, String> requestBody) {
        return given(기본_헤더값_설정()).log().all()
                .body(requestBody)
                .when().put(LINE_COMMAND_URL, lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String lineId) {
        return given(기본_헤더값_설정()).log().all()
                .when().delete(LINE_COMMAND_URL, lineId)
                .then().log().all()
                .extract();
    }
}
