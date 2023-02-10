package subway.Acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceStep {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(ExtractableResponse<Response> response) {
        return RestAssured.given()
                .pathParam("id", response.jsonPath().getLong("id"))
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}")
                .then().log().all()
                .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.jsonPath().getLong("id"))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList("id", Long.class);

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_제외됨(ExtractableResponse<Response> response) {
        String name = response.jsonPath().getString("name");

        ExtractableResponse<Response> responses = 지하철역_목록_조회();
        List<String> stationNames = responses.jsonPath().getList("name", String.class);

        assertThat(stationNames).doesNotContain(name);
    }
}
