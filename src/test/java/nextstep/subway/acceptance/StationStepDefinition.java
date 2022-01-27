package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDefinition {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철역_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static void 지하철역_생성_완료(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_목록_조회_완료(ExtractableResponse<Response> response, String ...stations) {
        지하철역_응답_상태_검증(response, HttpStatus.OK);
        assertThat(response.jsonPath().getList("name")).contains(stations);
    }

    public static void 지하철역_삭제_완료(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.NO_CONTENT);
    }

    public static void 지하철역_생성_실패(ExtractableResponse<Response> response) {
        지하철역_응답_상태_검증(response, HttpStatus.BAD_REQUEST);
    }
}
