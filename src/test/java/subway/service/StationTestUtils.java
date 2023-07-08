package subway.service;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationTestUtils {
    private StationTestUtils() {}

    public static void 지하철역_삭제(String stationUrl) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(stationUrl)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static String 지하철역_생성(Map<String, String> 지하철_정보) {

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(지하철_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.header("Location");
    }

    public static ExtractableResponse<Response> 지하철역_조회() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }
}
