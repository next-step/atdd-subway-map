package nextstep.subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionSteps {

    private static final String 상행역 = "upStationId";
    private static final String 하행역 = "downStationId";
    private static final String 역간_거리 = "distance";

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long 역1, Long 역2, int 거리) {
        Map<String, Object> params = new HashMap<>();
        params.put(상행역, 역1);
        params.put(하행역, 역2);
        params.put(역간_거리, 거리);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/lines/1/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제_요청(Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .param("stationId", stationId)
                .delete("/lines/1/sections")
                .then().log().all()
                .extract();
    }

    public static void 구간_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 구간_등록_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 구간_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 구간_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
