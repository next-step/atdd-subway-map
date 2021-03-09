package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionSteps {

    private static final String URI_SECTIONS = "/sections";
    private static final String HEADER_LOCATION = "Location";

    public static ExtractableResponse<Response> requestCreateSection(ExtractableResponse<Response> lineResponse,
                                                                     ExtractableResponse<Response> upStationResponse,
                                                                     ExtractableResponse<Response> downStationResponse) {
        Map<String, Object> params = makeSectionParams(upStationResponse, downStationResponse);
        String uri = lineResponse.header(HEADER_LOCATION);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri + URI_SECTIONS)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteSection(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header(HEADER_LOCATION);
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void assertCreateSection(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void assertCreateSectionFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static Map<String, Object> makeSectionParams(ExtractableResponse<Response> upStationResponse,
                                                         ExtractableResponse<Response> downStationResponse) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationResponse.jsonPath().getLong("id"));
        params.put("downStationId", downStationResponse.jsonPath().getLong("id"));
        params.put("distance", 1000);
        return params;
    }

    public static void assertDeleteSection(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void assertDeleteSectionFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
