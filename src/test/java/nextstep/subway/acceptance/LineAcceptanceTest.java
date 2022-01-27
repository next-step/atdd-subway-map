package nextstep.subway.acceptance;

import static nextstep.subway.utils.LineAcceptanceHelper.노선_생성;
import static nextstep.subway.utils.LineAcceptanceHelper.노선_정보_변경;
import static nextstep.subway.utils.LineAcceptanceHelper.노선_조회;
import static nextstep.subway.utils.StationAcceptanceTest.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineStationResponse;
import nextstep.subway.utils.CustomRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
        String upStationId = "1";
        String downStationId = "2";
        String distance = "123";

        //when
        ExtractableResponse<Response> result = 노선_생성(lineName, lineColor, upStationId, downStationId, distance);

        //then
        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.jsonPath().getString("name")).isEqualTo(lineName),
                () -> assertThat(result.jsonPath().getString("color")).isEqualTo(lineColor)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * <p>
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
        String upStationId1 = "1";
        String downStationId1 = "2";
        String distance1 = "123";

        노선_생성(lineName1, lineColor1, upStationId1, downStationId1, distance1);

        //given2 새로운 지하철 노선 생성
        String lineName2 = "이름2";
        String lineColor2 = "빨간색2";
        String upStationId2 = "1";
        String downStationId2 = "2";
        String distance2 = "123";

        노선_생성(lineName2, lineColor2, upStationId2, downStationId2, distance2);

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
        String 강남역_id = 지하철역_생성("강남역").jsonPath().getString("id");
        String 삼성역_id = 지하철역_생성("삼성역").jsonPath().getString("id");
        String distance = "123";
        String createdLineId = 노선_생성(lineName, lineColor, 강남역_id, 삼성역_id, distance).jsonPath().getString("id");


        //when
        ExtractableResponse<Response> result = 노선_조회(createdLineId);

        //then
        LineStationResponse lineStationResponse = result.as(LineStationResponse.class);
        assertAll(
                () -> assertThat(lineStationResponse.getName()).isEqualTo("이름"),
                () -> assertThat(lineStationResponse.getColor()).isEqualTo("빨간색"),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getId).containsExactly(Long.valueOf(강남역_id), Long.valueOf(삼성역_id)),
                () -> assertThat(lineStationResponse.getStationResponses()).extracting(LineStationResponse.Station::getName).containsExactly("강남역", "삼성역")
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
        String upStationId = "1";
        String downStationId = "2";
        String distance = "123";
        String updateLineName = "이름2";
        String updateLineColor = "빨간색2";

        String createdLineId = 노선_생성(lineName, lineColor, upStationId, downStationId, distance).jsonPath().getString("id");

        //when
        ExtractableResponse<Response> updateLineResult = 노선_정보_변경(createdLineId, updateLineName, updateLineColor);

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
        String upStationId = "1";
        String downStationId = "2";
        String distance = "123";

        String createdLineId = 노선_생성(lineName, lineColor, upStationId, downStationId, distance).jsonPath().getString("id");

        //when
        ExtractableResponse<Response> deleteResult = CustomRestAssured.delete("/lines/" + createdLineId);

        //then
        assertThat(deleteResult.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> searchResult = 노선_조회(createdLineId);
        assertThat(searchResult.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("같은 이름의 지하철 노선은 1개만 존재 가능하다.")
    @Test
    void createDuplicateLine() {
        //given1 지하철 노선 생성
        String lineName = "이름";
        String lineColor = "빨간색";
        String upStationId = "1";
        String downStationId = "2";
        String distance = "123";
        노선_생성(lineName, lineColor, upStationId, downStationId, distance);

        //when
        ExtractableResponse<Response> result = 노선_생성(lineName, lineColor, upStationId, downStationId, distance);

        //then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 이름을 업데이트 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("이미 존재하는 노선 이름으로 이름을 변경 할 수 없다.")
    @Test
    void updateDuplicateLine() {
        //given1 지하철 노선 생성
        String firstLineName = "이름1";
        String lineColor = "빨간색";
        String secondLineName = "이름2";
        String upStationId = "1";
        String downStationId = "2";
        String distance = "123";

        노선_생성(firstLineName, lineColor, upStationId, downStationId, distance);
        String createdLineId = 노선_생성(secondLineName, lineColor, upStationId, downStationId, distance).jsonPath().getString("id");

        //when
        ExtractableResponse<Response> result = 노선_정보_변경(createdLineId, firstLineName, lineColor);

        //then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
