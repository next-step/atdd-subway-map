package subway.executor;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

public class LineAcceptanceExecutor {

    public static ExtractableResponse<Response> createLine(LineRequest lineRequest) {

        return RestAssured.given().body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> showLines() {

        return RestAssured
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> showLine(Long id) {

        return RestAssured
                .given()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long id, LineRequest request) {

        return RestAssured
                .given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {

        return RestAssured
                .given()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }


}
