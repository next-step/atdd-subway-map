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

public class LineAcceptanceTestHelper extends AcceptanceTestHelper {

    static ExtractableResponse<Response> 노선_생성_요청(final String name, final String color,
                                                         final long upStationId, final long downStationId, final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    static void 노선이_정상적으로_생성되었는지_확인(final ExtractableResponse<Response> response, final String lineName) {
        응답_코드_검증(response, HttpStatus.CREATED);

        final List<String> 노선_목록_조회함 = 노선_목록_조회함();
        assertThat(노선_목록_조회함).containsAnyOf(lineName);
    }

    static Long 노선_생성함(final String name, final String color,
                       final long upStationId, final long downStationId, final int distance) {
        final ExtractableResponse<Response> response = 노선_생성_요청(name, color, upStationId, downStationId, distance);
        응답_코드_검증(response, HttpStatus.CREATED);
        return response.jsonPath().getLong("id");
    }

    static ExtractableResponse<Response> 노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    static void 노선들이_목록_안에_있는지_확인(final ExtractableResponse<Response> response, final String... lineNames) {
        응답_코드_검증(response, HttpStatus.OK);
        final List<String> 노선_이름_목록 = response.jsonPath().getList("name", String.class);
        assertThat(노선_이름_목록).containsOnly(lineNames);
    }

    static List<String> 노선_목록_조회함() {
        final ExtractableResponse<Response> response = 노선_목록_조회_요청();
        응답_코드_검증(response, HttpStatus.OK);
        return response.jsonPath().getList("name", String.class);
    }
}
