package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTestHelper {

    static ExtractableResponse<Response> 지하철역_생성_요청(final Map<String, Object> params) {
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

    static String 지하철역_생성함(final Map<String, Object> params) {
        final ExtractableResponse<Response> response = 지하철역_생성_요청(params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("location");
    }

    static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    static void 지하철역들이_목록_안에_있는지_확인(final ExtractableResponse<Response> response, final String... stations) {
        응답_코드_검증(response, HttpStatus.OK);

        final List<String> 지하철역_이름_목록 = response.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).containsOnly(stations);
    }

    static List<String> 지하철역_목록_조회함() {
        final ExtractableResponse<Response> response = 지하철역_목록_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    static ExtractableResponse<Response> 지하철역_삭제_요청(final String location) {
        return RestAssured
                .given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    static void 지하철역이_정상적으로_삭제되었는지_확인(final ExtractableResponse<Response> response, final String stationName) {
        응답_코드_검증(response, HttpStatus.NO_CONTENT);

        final List<String> 지하철역_이름_목록 = 지하철역_목록_조회함();
        assertThat(지하철역_이름_목록).doesNotContain(stationName);
    }

    private static void 응답_코드_검증(final ExtractableResponse<Response> response, final HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }
}
