package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTestHelper extends AcceptanceTestHelper {

    static ExtractableResponse<Response> 지하철역_생성_요청(final String name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    static void 지하철역이_정상적으로_생성되었는지_확인(final ExtractableResponse<Response> response, final String stationName) {
        응답_코드_검증(response, HttpStatus.CREATED);

        final List<String> 지하철역_이름_목록 = 지하철역_목록_조회함();
        assertThat(지하철역_이름_목록).containsAnyOf(stationName);
    }

    static Long 지하철역_생성함(final String name) {
        final ExtractableResponse<Response> response = 지하철역_생성_요청(name);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.jsonPath().getLong("id");
    }

    static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    static void 지하철역들이_목록_안에_있는지_확인(final ExtractableResponse<Response> response, final String... stationNames) {
        응답_코드_검증(response, HttpStatus.OK);

        final List<String> 지하철역_이름_목록 = response.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).containsOnly(stationNames);
    }

    static List<String> 지하철역_목록_조회함() {
        final ExtractableResponse<Response> response = 지하철역_목록_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    static ExtractableResponse<Response> 지하철역_삭제_요청(final Long stationId) {
        return RestAssured
                .given().log().all()
                .when().delete("/stations/{stationId}", stationId)
                .then().log().all()
                .extract();
    }

    static void 지하철역이_정상적으로_삭제되었는지_확인(final ExtractableResponse<Response> response, final String stationName) {
        응답_코드_검증(response, HttpStatus.NO_CONTENT);

        final List<String> 지하철역_이름_목록 = 지하철역_목록_조회함();
        assertThat(지하철역_이름_목록).doesNotContain(stationName);
    }
}
