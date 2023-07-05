package subway.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTestUtils {

    private LineTestUtils() {}

    public static ExtractableResponse<Response> 지하철_노선_생성(Map<String, String> 노선_생성_요청_정보, Map<String, String> 상행역_정보, Map<String, String> 하행역_정보) {
        노선_생성_요청_정보.put("upStationId", 상행역_정보.get("id"));
        노선_생성_요청_정보.put("downStationId", 하행역_정보.get("id"));

        StationTestUtils.지하철역_생성(상행역_정보);
        StationTestUtils.지하철역_생성(하행역_정보);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(노선_생성_요청_정보)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .accept(ContentType.JSON)
                .get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(String lineUrl) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .accept(ContentType.JSON)
                .get(lineUrl)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String lineUrl, Map<String, String> 노선_수정_요청_정보) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(노선_수정_요청_정보)
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put(lineUrl)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제(String lineUrl) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete(lineUrl)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        return response;
    }
}
