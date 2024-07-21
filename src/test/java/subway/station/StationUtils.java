package subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StationUtils {

    public static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = createParams(stationName);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록조회() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static void 지하철역_삭제(String location) {
        RestAssured.given().log().all()
            .when().delete(location)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static Map<String, String> createParams(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return params;
    }
}
