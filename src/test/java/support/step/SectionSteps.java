package support.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import support.fixture.SectionFixture;

public class SectionSteps {

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, SectionFixture fixture) {
        return RestAssured.given()
            .body(fixture)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{lineId}/sections", lineId)
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }
}
