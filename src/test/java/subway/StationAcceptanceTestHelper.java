package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTestHelper {

    static ExtractableResponse<Response> 지하철역_생성_요청(final Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    static String 지하철역_생성함(final Map<String, Object> params) {
        final ExtractableResponse<Response> response = 지하철역_생성_요청(params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("location");
    }

    static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    static List<String> 지하철역_목록_조회함() {
        final ExtractableResponse<Response> response = 지하철역_목록_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    static ExtractableResponse<Response> 지하철역_삭제_요청(final String location) {
        return RestAssured
                .given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }
}
