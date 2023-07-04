package subway.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //when
        final String 신분당선 = "신분당선";
        지하철_노선_생성(신분당선);

        //then
        assertThat(getSubwayLines()).containsExactly(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void showLines() {
        //given
        final String 신분당선 = "신분당선";
        final String 분당선 = "분당선";
        지하철_노선_여러개_생성(List.of(신분당선, 분당선));

        //when
        List<String> subwayLines = getSubwayLines();

        //then
        assertThat(subwayLines).containsOnly(분당선, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void showLine() {
        //given
        final String 신분당선 = "신분당선";
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철_노선_생성(신분당선);

        //when
        ExtractableResponse<Response> 신분당선_조회_응답 = getSubwayLine(getIdFromResponse(신분당선_생성_응답));
        LineResponse lineResponse = 신분당선_조회_응답.as(LineResponse.class);

        //then
        assertThat(lineResponse.getName()).isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        //given
        final String 신분당선 = "신분당선";
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철_노선_생성(신분당선);

        //when
        final String 다른분당선 = "다른분당선";
        LineUpdateRequest request = new LineUpdateRequest(다른분당선, "bg-red-600");
        ExtractableResponse<Response> 다른분당선_수정_응답 = updateSubwayLine(getIdFromResponse(신분당선_생성_응답), request);
        LineResponse lineResponse = 다른분당선_수정_응답.as(LineResponse.class);

        //then
        assertThat(lineResponse.getName()).isEqualTo(다른분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        //given
        final String 신분당선 = "신분당선";
        ExtractableResponse<Response> 신분당선_생성_응답 = 지하철_노선_생성(신분당선);

        //when
        ExtractableResponse<Response> 신분당선_삭제_응답 = deleteSubwayLine(getIdFromResponse(신분당선_생성_응답));

        //then
        assertThat(신분당선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> deleteSubwayLine(Long lineId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().delete("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateSubwayLine(Long lineId, LineUpdateRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", lineId)
                .when().put("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getSubwayLine(Long lineId) {
        return RestAssured
                .given().log().all()
                .pathParam("id", lineId)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_생성(final String name) {
        final String 상행종점역 = "상행좀점역";
        final String 하행종점역 = "하행종점역";

        LineCreateRequest request = new LineCreateRequest(
                name,
                "bg-red-600",
                getIdFromResponse(AcceptanceTestUtil.create("/stations", new StationRequest(상행종점역))),
                getIdFromResponse(AcceptanceTestUtil.create("/stations", new StationRequest(하행종점역))),
                10L
        );
        return AcceptanceTestUtil.create("/lines", request);
    }

    private void 지하철_노선_여러개_생성(final List<String> names) {
        names.forEach(name -> 지하철_노선_생성(name));
    }

    private Long getIdFromResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("id", Long.class);
    }

    private List<String> getSubwayLines() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name");
    }

}
