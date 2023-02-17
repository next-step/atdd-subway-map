package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dtos.request.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class LineApiTest {
    public static ExtractableResponse<Response> createLine(LineRequest lineRequest) {
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;

    }

    public static List<String> getLineNames() {
        List<String> lineNames = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
        return lineNames;
    }

    public static String getLineName(Long id) {
        String lineName = RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().getString("name");
        return lineName;
    }

    public static ExtractableResponse<Response> updateLine(LineRequest updateRequest, Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log(). all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateRequest)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        return response;
    }

}
