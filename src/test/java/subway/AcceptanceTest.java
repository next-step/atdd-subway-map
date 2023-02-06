package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    ExtractableResponse<Response> 지하철역을_생성한다(String stationName) {
        return RestAssured.given().log().all()
            .body(Map.of("name", stationName))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    ExtractableResponse<Response> 지하철역_목록을_조회한다() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    ExtractableResponse<Response> 지하철역을_삭제한다(Long stationId) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/stations/{stationId}", stationId)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    ExtractableResponse<Response> 노선을_생성한다(
        String name,
        String color,
        String upStationName,
        String downStationName,
        int distance
    ) {
        final long upStationId = 지하철역을_생성한다(upStationName).body().jsonPath().getLong("id");
        final long downStationId = 지하철역을_생성한다(downStationName).body().jsonPath().getLong("id");

        final Map<String, Object> params = Map.of(
            "name", name,
            "color", color,
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", containsString("lines"))
            .extract();
    }

    ExtractableResponse<Response> 노선을_생성한다(
        String name,
        String color,
        long upStationId,
        long downStationId,
        int distance
    ) {

        final Map<String, Object> params = Map.of(
            "name", name,
            "color", color,
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", containsString("lines"))
            .extract();
    }

    ExtractableResponse<Response> 노선_목록을_조회한다() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    ExtractableResponse<Response> 노선을_조회한다(long lineId) {
        return RestAssured.given().log().all()
            .when().get("/lines/{id}", lineId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    ExtractableResponse<Response> 노선을_수정한다(
        long lineId,
        String name,
        String color
    ) {
        final Map<String, Object> params = Map.of(
            "name", name,
            "color", color
        );
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/lines/{id}", lineId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    void 노선을_삭제한다(long lineId) {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/lines/{id}", lineId)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    ExtractableResponse<Response> 구간을_추가_요청(
        long lineId,
        long upStationId,
        long downStationId,
        int distance
    ) {
        final Map<String, Object> params = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines/{lineId}/sections", lineId)
            .then().log().all()
            .extract();
    }

}
