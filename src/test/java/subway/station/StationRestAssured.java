package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StationRestAssured {

    public static List<ExtractableResponse<Response>> 역_생성(final String... stationNames) {
        return Arrays.stream(stationNames)
                .map(StationRestAssured::역_생성)
                .collect(Collectors.toUnmodifiableList());
    }

    public static ExtractableResponse<Response> 역_생성(final String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 역_삭제(final Long id) {
        return givenLog()
                .when()
                .delete("/stations/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 역_목록_조회() {
        return givenLog()
                .when()
                .get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private static RequestSpecification givenLog() {
        return RestAssured
                .given().log().all();
    }
}
