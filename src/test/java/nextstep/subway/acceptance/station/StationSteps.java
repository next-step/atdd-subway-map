package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StationSteps {

    public static final String GANGNAM_STATION_NAME = "강남역";
    public static final String YUKSAM_STATION_NAME = "역삼역";
    public static final String NONHYUN_STATION_NAME = "논현역";
    public static final String SHIN_NONHYUN_STATION_NAME = "신논현역";


    public static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static Long 지하철역_생성_ID(String stationName) {
        Map<String, String> param = new HashMap<>();
        param.put("name", stationName);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getLong("id");
    }

    public static Long ID(ExtractableResponse<Response> 역) {
        return Long.valueOf(역.jsonPath().getString("id"));
    }

    public static void 노선_생성_검증(ExtractableResponse<Response> response) {
        assertAll(
                // then 지하철역이 생성된다
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
                () -> assertThat(지하철역_목록_조회().jsonPath().getList("name")).containsAnyOf(GANGNAM_STATION_NAME)
        );
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static void 역_삭제_검증(String stationName) {
        List<String> stationNames = 지하철역_목록_조회().jsonPath().getList("name");
        assertThat(stationNames).doesNotContain(stationName);
    }
}