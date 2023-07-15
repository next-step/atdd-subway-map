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
                    .post(SUBWAY_SECTION_URL)
                .then().log().all()
                .extract();

        return createSectionResponse;
    }
}