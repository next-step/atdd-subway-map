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

import static nextstep.subway.acceptance.StationAcceptanceTest.지하철_역_생성_요청;
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
        //given
        지하철_역_생성_요청("강남");
        지하철_역_생성_요청("역삼");
        // when
        ExtractableResponse response = 지하철_노선_생성_요청("신분당선", "bg-red-600",
                1L, 2L, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
//        given
        지하철_역_생성_요청("강남");
        지하철_역_생성_요청("역삼");
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_노선_생성_요청("2호선", "bg-red-600", 1L, 2L, 20);
        지하철_노선_생성_요청("신분당선", "bg-green-600", 3L, 4L, 30);

//        when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

//        then
        List<String> linesName = response.jsonPath().getList("name");
        assertThat(linesName).contains("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_노선_생성_요청("신분당선", "bg-red-600", 1L, 2L, 20);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .when()
                .get("/lines/{id}")
                .then().log().all()
                .extract();

        String lineName = response.jsonPath().getString("name");
        assertThat(lineName).contains("신분당선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_노선_생성_요청("신분당선", "bg-red-600", 1L, 2L, 20);

        // when
        Map<String, String> updateLineParam = new HashMap<>();
        updateLineParam.put("name", "2호선");
        updateLineParam.put("color", "bg-green-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .body(updateLineParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        String lineName = response.jsonPath().getString("name");
        String lineColor = response.jsonPath().getString("color");
        assertThat(lineName).isEqualTo("2호선");
        assertThat(lineColor).isEqualTo("bg-green-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_노선_생성_요청("신분당선", "bg-red-600", 1L, 2L, 20);

        //when
        ExtractableResponse response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 중복된 노선 생성을 요청하면
     * Then 500 error 가 터진다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성 시 실패")
    @Test
    void createDuplicatedLine() {
        // given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_역_생성_요청("양재");

        지하철_노선_생성_요청("신분당선", "green", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "green",
                2L, 3L, 20);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성을 한다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void addSection() {
        // given
        지하철_역_생성_요청("강남");
        지하철_역_생성_요청("역삼");
        지하철_역_생성_요청("건대");

        지하철_노선_생성_요청("2호선", "green", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(1L, 2L, 3L, 20);
        // then
        assertThat(response.jsonPath().getString("sections").contains("강남")).isTrue();
        assertThat(response.jsonPath().getString("sections").contains("역삼")).isTrue();
        assertThat(response.jsonPath().getString("sections").contains("건대")).isTrue();
        assertThat(response.jsonPath().getString("sections").contains("양재")).isFalse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSection() {
        //given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_역_생성_요청("양재");
        지하철_역_생성_요청("양재시민의역");
        지하철_노선_생성_요청("신분당선", "bg-red-600", 1L, 2L, 20);
        지하철_구간_생성_요청(1L, 2L, 3L, 30);

        //when
        ExtractableResponse response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/sections/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 역 생성과 노선 생성을 요창허고
     * When 생성한 지하철 노선의 하행역과 다른 상행역 구간 생성을 요청하면
     * Then 지하철 구간 생성 요청이 실패한다.
     */
    @DisplayName("기존 노선의 하행역과 다른 상행역 구간 생성 요청 시 실패")
    @Test
    void 기존_하행역과_다른_상행역_구간_생성_요청() {
        // given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_역_생성_요청("양재");
        지하철_역_생성_요청("앵재시민의역");
        지하철_노선_생성_요청("신분당선", "green", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(1L, 3L, 4L,
                20);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 역, 노선, 구간 생성을 요청하고
     * When 노선에 이미 존재하는 역을, 하행역으로 포함하는 구간 생성 요창허면
     * Then 지하철 구간 생성 요청이 실패한다.
     */
    @DisplayName("노선에 포함된 하행역을 가진 구간 생성 요청 시 실패")
    @Test
    void 포함된_하행역_구간_생성_요청() {
        // given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_역_생성_요청("양재");
        지하철_역_생성_요청("앵재시민의역");
        지하철_노선_생성_요청("신분당선", "green", 1L, 2L, 10);
        지하철_구간_생성_요청(1L, 2L, 3L, 30);

        // when
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(1L, 3L, 1L,
                20);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 역과 노선 생성을 요청하고
     * When 한 개 이하의 구간을 가지는 노선에 구간 삭제를 요창허면
     * Then 지하철 구간 삭제 요청이 실패한다.
     */
    @DisplayName("한 개 이하의 구간을 가진 노선에 구간 삭제 요청 시 실패")
    @Test
    void 한개_이하_지하철_구간_삭제() {
        //given
        지하철_역_생성_요청("미금");
        지하철_역_생성_요청("정자");
        지하철_노선_생성_요청("신분당선", "bg-red-600", 1L, 2L, 20);

        //when
        ExtractableResponse response = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/sections/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, Long upStationId,
                                                      Long downStationId, int distance) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", Integer.toString(distance));

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all().extract();
    }

    public ExtractableResponse<Response> 지하철_구간_생성_요청(Long lineId, Long upStationId,
                                                      Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", Long.toString(upStationId));
        params.put("downStationId", Long.toString(downStationId));
        params.put("distance", Integer.toString(distance));

        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }
}


