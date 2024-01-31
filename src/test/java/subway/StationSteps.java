package subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

public class StationSteps {

    static void 지하철_역_생성_요청(List<StationRequest> requests) {
        requests.forEach(StationSteps::지하철_역_생성_요청);
    }

    static void 지하철_역_생성_요청(StationRequest request) {
        given()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/stations")
        .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
