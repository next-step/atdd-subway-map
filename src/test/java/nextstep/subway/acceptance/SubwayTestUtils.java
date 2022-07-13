package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import org.springframework.http.MediaType;

public class SubwayTestUtils {

    public static ExtractableResponse<Response> createStationWithName(String stationName) {
        var requestBody = new HashMap<String, String>();
        requestBody.put("name", stationName);

        return RestAssured
                .given()
                    .body(requestBody)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then()
                    .extract();
    }

    public static ExtractableResponse<Response> createLine(LineCreationRequest creationRequest) {
        return RestAssured
                .given()
                    .body(creationRequest, ObjectMapperType.JACKSON_2)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .extract();
    }

    public static ExtractableResponse<Response> getLine(Long lineId) {
        return RestAssured
                .given()
                    .pathParam("lineId", lineId)
                .when()
                    .get("/lines/{lineId}")
                .then()
                    .extract();
    }

    public static ExtractableResponse<Response> getAllLines() {
        return RestAssured
                .when()
                    .get("/lines")
                .then()
                    .extract();
    }

    public enum STATIONS {
        광교역,
        광교중앙역,
        상현역,
        성복역,
        수지구청역;
    }
}
