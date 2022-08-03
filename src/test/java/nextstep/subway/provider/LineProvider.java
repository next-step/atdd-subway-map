package nextstep.subway.provider;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class LineProvider extends Provider {

    public static ExtractableResponse<Response> 노선_생성_요청(String lineName, String color, Long upStationId, Long downStationId, Integer distance) {
        final Map<String, Object> params = 요청보낼_파라미터_생성(lineName, color, upStationId, downStationId, distance);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();

        return response;
    }

    public static Long 노선_생성됨(String lineName, String color, Long upStationId, Long downStationId, Integer distance) {
        final ExtractableResponse<Response> 지하철노선_생성_응답 = 노선_생성_요청(lineName, color, upStationId, downStationId, distance);
        assertThat(지하철노선_생성_응답.statusCode()).isEqualTo(CREATED.value());
        return 지하철노선_생성_응답.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 노선_목록_조회_요청() {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 노선_상세_정보_조회_요청(Long lineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 노선_상세_정보_조회됨(Long lineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(OK.value());

        return response;
    }

    public static ExtractableResponse<Response> 노선_정보_변경_요청(Long lineId, String name, String color) {
        final Map<String, Object> params = 요청보낼_파라미터_생성(name, color);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/{lineI}", lineId)
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(Long lineId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        return response;
    }

    public static void 노선이_정상적으로_생성되었는지_확인(ExtractableResponse<Response> response) {
        final List<Object> stations = response.jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
    }

    public static void 노선_목록에_생성한_지하철노선이_있는지_확인(String createdLineName) {
        final ExtractableResponse<Response> getLinesResponse = 노선_목록_조회_요청();
        final List<String> subwayLineNames = getLinesResponse.jsonPath().getList("name", String.class);
        assertThat(subwayLineNames).contains(createdLineName);
    }

    public static void 노선_목록에_생성한_지하철노선이_있는지_확인(List<String> createdLineNames) {
        final ExtractableResponse<Response> getLinesResponse = 노선_목록_조회_요청();
        final List<String> subwayLineNames = getLinesResponse.jsonPath().getList("name", String.class);
        assertThat(subwayLineNames).containsAll(createdLineNames);
    }

    public static void 조회한_노선이_생성한_노선인지_확인(Long 생성된_노선_아이디, ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답) {
        final long 조회한_지하철노선_고유_번호 = 지하철노선_상세_정보_조회_응답.jsonPath().getLong("id");
        assertThat(조회한_지하철노선_고유_번호).isEqualTo(생성된_노선_아이디);
    }

    public static void 노선_변경이_잘_이루어졌는지_확인(String name, String color, ExtractableResponse<Response> 지하철노선_상세_정보_조회_응답) {
        assertThat(지하철노선_상세_정보_조회_응답.jsonPath().getString("name")).isEqualTo(name);
        assertThat(지하철노선_상세_정보_조회_응답.jsonPath().getString("color")).isEqualTo(color);
    }

    public static void 노선이_정상적으로_삭제되었는지_확인() {
        final ExtractableResponse<Response> 지하철노선_목록_조회_응답 = 노선_목록_조회_요청();
        final List<Object> subwayLineIdList = 지하철노선_목록_조회_응답.jsonPath().getList("id");
        assertThat(subwayLineIdList).hasSize(0);
    }

    private static Map<String, Object> 요청보낼_파라미터_생성(String name, String color) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }

    private static Map<String, Object> 요청보낼_파라미터_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return params;
    }
}
