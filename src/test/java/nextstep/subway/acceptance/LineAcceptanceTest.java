package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.CustomRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

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
        String lineName = "이름";
        String lineColor = "빨간색";

        //when
        ExtractableResponse<Response> result = 노선_생성(lineName, lineColor);

        //then
        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.jsonPath().getString("name")).isEqualTo(lineName),
                () -> assertThat(result.jsonPath().getString("color")).isEqualTo(lineColor)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     *
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given1 지하철 노선 생성
        String lineName1 = "이름";
        String lineColor1 = "빨간색";

        노선_생성(lineName1, lineColor1);

        //given2 새로운 지하철 노선 생성
        String lineName2 = "이름2";
        String lineColor2 = "빨간색2";

        노선_생성(lineName2, lineColor2);

        //when
        ExtractableResponse<Response> result = CustomRestAssured.get("/lines/");

        //then
        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result.jsonPath().getList("name")).containsExactly(lineName1, lineName2),
                () -> assertThat(result.jsonPath().getList("color")).containsExactly(lineColor1, lineColor2)
        );
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
        String lineName = "이름";
        String lineColor = "빨간색";
        String createdLineId = 노선_생성(lineName, lineColor).jsonPath().getString("id");

        //when
        ExtractableResponse<Response> result = 노선_조회(createdLineId);

        //then
        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result.jsonPath().getString("name")).isEqualTo(lineName),
                () -> assertThat(result.jsonPath().getString("color")).isEqualTo(lineColor)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given1 지하철 노선 생성
        String lineName = "이름";
        String lineColor = "빨간색";
        String updateLineName = "이름2";
        String updateLineColor = "빨간색2";

        String createdLineId = 노선_생성(lineName, lineColor).jsonPath().getString("id");

        //when
        ExtractableResponse<Response> updateLineResult =
                CustomRestAssured.put("/lines/" + createdLineId, createParams(updateLineName, updateLineColor));

        //then
        ExtractableResponse<Response> searchResult = 노선_조회(createdLineId);

        assertAll(
                () -> assertThat(updateLineResult.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(searchResult.jsonPath().getString("name")).isEqualTo(updateLineName),
                () -> assertThat(searchResult.jsonPath().getString("color")).isEqualTo(updateLineColor)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given1 지하철 노선 생성
        String lineName = "이름";
        String lineColor = "빨간색";

        String createdLineId = 노선_생성(lineName, lineColor).jsonPath().getString("id");

        //when
        ExtractableResponse<Response> deleteResult = CustomRestAssured.delete("/lines/" + createdLineId);

        //then
        assertThat(deleteResult.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> searchResult = 노선_조회(createdLineId);
        assertThat(searchResult.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 노선_조회(final String createdLineId) {
        return CustomRestAssured.get("/lines/" + createdLineId);
    }

    private ExtractableResponse<Response> 노선_생성(final String lineName, final String lineColor) {
        return CustomRestAssured.post("/lines/", createParams(lineName, lineColor));
    }

    private Map<String, String> createParams(final String lineName, final String lineColor) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        return params;
    }
}
