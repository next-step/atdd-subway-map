package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StationSteps {
    
    public static ExtractableResponse<Response> requestCreateStationGangnam() {
        Map<String, String> params = makeStationParams("강남역");
        return requestCreateStation(params);
    }

    public static ExtractableResponse<Response> requestCreateStationYeoksam() {
        Map<String, String> params = makeStationParams("역삼역");
        return requestCreateStation(params);
    }

    public static ExtractableResponse<Response> requestCreateStation(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestGetStations() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestDeleteStation(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void assertCreateStation(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void assertCreateStationFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void assertGetStations(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void assertIncludeStations(ExtractableResponse<Response> createResponse1, ExtractableResponse<Response> createResponse2, ExtractableResponse<Response> response) {
        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void assertDeleteStation(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static Map<String, String> makeStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }
}
