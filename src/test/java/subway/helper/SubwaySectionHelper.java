package subway.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SubwaySectionHelper {

    public static final String SUBWAY_SECTION_URL = "/subway-sections";

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Map<String, Object> createSectionRequest) {
        ExtractableResponse<Response> createSectionResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(createSectionRequest)
                .when().log().all()
                    .post(SUBWAY_SECTION_URL + "/1")
                .then().log().all()
                .extract();

        return createSectionResponse;
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Map<String, Object> registerSectionRequest) {
        ExtractableResponse<Response> registerSectionResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(registerSectionRequest)
                .when().log().all()
                    .post(SUBWAY_SECTION_URL + "/register")
                .then().log().all()
                .extract();

        return registerSectionResponse;
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(String lineId) {
        ExtractableResponse<Response> deleteSectionResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .delete(SUBWAY_SECTION_URL + "/" + lineId)
                .then().log().all()
                .extract();

        return deleteSectionResponse;
    }
}