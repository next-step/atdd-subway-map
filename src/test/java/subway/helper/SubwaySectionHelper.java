package subway.helper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class SubwaySectionHelper {

    public static final String SUBWAY_SECTION_URL = "/subway-sections";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청_임시(Map<String, Object> createLineRequest) {
        return RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(createLineRequest)
                .when().log().all()
                    .post("/lines")
                .then().log().all()
                .extract();
    }

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

    public static ExtractableResponse<Response> 지하철_구간_추가_요청_임시(Long lineId,
                                                                Map<String, Object> registerSectionRequest) {
        return RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON.withCharset("UTF-8"))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(registerSectionRequest)
                .when().log().all()
                    .post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long lineId, Long stationId) {
        ExtractableResponse<Response> deleteSectionResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .delete("lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();

        return deleteSectionResponse;
    }
}