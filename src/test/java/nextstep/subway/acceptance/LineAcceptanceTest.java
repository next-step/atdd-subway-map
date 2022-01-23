package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.utils.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Map<String, String> params = createLineParam(RED_COLOR_LINE, 신분당선);

        // when
        ExtractableResponse<Response> response = createLineResponse(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION_HEADER)).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        createLineResponse(createLineParam(RED_COLOR_LINE, 신분당선));
        createLineResponse(createLineParam(GREEN_COLOR_LINE, 이호선));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get(LINE_PATH)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList(NAME_PARAMETER);
        assertThat(lineNames).contains(신분당선, 이호선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = createLineResponse(createLineParam(RED_COLOR_LINE, 신분당선));

        // when
        String uri = createResponse.header(LOCATION_HEADER);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String lineName = response.jsonPath().get(NAME_PARAMETER);
        assertThat(lineName).isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = createLineResponse(createLineParam(RED_COLOR_LINE, 신분당선));

        // when
        Map<String, String> newParam = createLineParam(BLUE_COLOR_LINE, 구분당선);

        String uri = createResponse.header(LOCATION_HEADER);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(newParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String lineName = response.jsonPath().get(NAME_PARAMETER);
        assertThat(lineName).isEqualTo(구분당선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = createLineResponse(createLineParam(RED_COLOR_LINE, 신분당선));

        // when
        String uri = createResponse.header(LOCATION_HEADER);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> createLineParam(String color, String name) {
        Map<String, String> params = new HashMap<>();
        params.put(COLOR_PARAMETER, color);
        params.put(NAME_PARAMETER, name);
        return params;
    }

    private ExtractableResponse<Response> createLineResponse(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(LINE_PATH)
                .then().log().all()
                .extract();
    }
}
