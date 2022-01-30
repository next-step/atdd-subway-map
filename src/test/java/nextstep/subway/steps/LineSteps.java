package nextstep.subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fixture.TLine;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    private static final String 노선_이름 = "name";
    private static final String 노선_색 = "color";
    private static final String 노선_상행역 = "upStationId";
    private static final String 노선_하행역 = "downStationId";
    private static final String 노선_거리 = "distance";

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(TLine line) {
        Map<String, Object> params = new HashMap<>();
        params.put(노선_이름, line.이름);
        params.put(노선_색, line.색);
        params.put(노선_상행역, line.상행역);
        params.put(노선_하행역, line.하행역);
        params.put(노선_거리, line.거리);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_변경_요청(String uri, TLine line) {
        Map<String, String> params = new HashMap<>();
        params.put(노선_이름, line.이름);
        params.put(노선_색, line.색);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 노선_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
    }

    public static void 노선_목록_조회_성공(ExtractableResponse<Response> response, TLine line1, TLine line2) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList(노선_이름);
        assertThat(lineNames).containsExactly(line1.이름, line2.이름);
        List<String> lineColors = response.jsonPath().getList(노선_색);
        assertThat(lineColors).containsExactly(line1.색, line2.색);
    }

    public static void 노선_조회_성공(ExtractableResponse<Response> response, TLine line) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = response.jsonPath().get(노선_이름);
        assertThat(name).isEqualTo(line.이름);
        String color = response.jsonPath().get(노선_색);
        assertThat(color).isEqualTo(line.색);
    }

    public static void 노선_조회_실패_없는_노선(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 노선_변경_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 노선_변경_실패_없는_노선(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 노선_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 노선_생성_실패_중복(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
