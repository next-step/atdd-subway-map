package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineAcceptanceUtil {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest requestBody) {
        // when
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, LineRequest requestBody) {
        String location = response.header("location");

        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .put(location)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_모든_노선_조회_요청() {
        return RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_특정_노선_조회_요청(ExtractableResponse<Response> response) {
        String location = response.header("location");

        return RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(location)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(ExtractableResponse<Response> response) {
        String location = response.header("location");

        return RestAssured
                .given()
                .when()
                .delete(location)
                .then()
                .log()
                .all()
                .extract();

    }
}
