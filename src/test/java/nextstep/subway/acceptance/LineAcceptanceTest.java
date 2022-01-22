package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.steps.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        Map<String, String> params = new HashMap<>();
        params.put("color", "bg-green");
        params.put("name", "2호선");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(jsonPath("$.color", is(params.get("color"))));
        assertThat(jsonPath("$.name", is(params.get("name"))));
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
        지하철노선_생성요청("2호선", "bg-green");
        지하철노선_생성요청("3호선", "bg-orange");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        List<Integer> lineIds = response.jsonPath().getList("id");

        /*
           목록을 조회할 때 PK까지만 검증을 하면 되는건지 (75번 라인만 검증해도 되는건지)
           아니면 이름도 중요한 거 같으니 이름까지 검증을 하면 되는건지 리뷰어님의 의견이 궁금합니다.
           저의 경우는 지하철 라인의 경우 이름도 중요한 거 같아 이름까지 검증해야 한다고 생각합니다.
         */
        assertThat(lineIds).contains(1, 2);
        assertThat(lineNames).contains("2호선", "3호선");
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
        ExtractableResponse<Response> lineCreateResponse = 지하철노선_생성요청("2호선", "bg-green");
        Long lineId = lineCreateResponse.jsonPath().getLong("id");
        String lineName = lineCreateResponse.jsonPath().get("name");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath("$.name", is(lineName)));
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
        /*
            지하철노선_생성요청의 경우 리턴타입이 ExtractableResponse<Response>인데 이경우
            Long lineId = createLineResponse.jsonPath().getLong("id"); 이렇게 코딩하여 불편하였습니다.

            그래서 지하철노선_생성요청2의 경우 리턴타입을 LineResponse로 하여
            Long lineId = createLineResponse.getId(); 이렇게 간단하게 바꾸게 하였는데 의견이 궁금합니다.
         */
        LineResponse createLineResponse = 지하철노선_생성요청2("2호선", "bg-green");
        Long lineId = createLineResponse.getId();

        Map<String, String> params = new HashMap<>();
        params.put("name", "신1호선");
        params.put("color", "bg-blue-200");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + lineId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath("$.name", is("신1호선")));
        assertThat(jsonPath("$.color", is("bg-blue-200")));
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
        ExtractableResponse<Response> createLineResponse = 지하철노선_생성요청("2호선", "bg-green");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(createLineResponse.header("Location"))
                .then().log().all().extract();

        ExtractableResponse<Response> findLineResponse = 지하철노선_단건조회(createLineResponse.jsonPath().getLong("id"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(findLineResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
