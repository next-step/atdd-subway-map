package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.common.CommonLineAcceptance.*;
import static nextstep.subway.acceptance.common.CommonStationAcceptance.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 상,하행역이 주어짐
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //given
        String 상행역 = "뚝섬역";
        String upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // when
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(
                신분당선, 빨강색, "100", upStationId, downStationId);

        // then
        String responseName = response.jsonPath().getString("name");
        String responseColor = response.jsonPath().getString("color");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responseName).isEqualTo(신분당선);
        assertThat(responseColor).isEqualTo(빨강색);
        assertThat(response.header("location")).isNotEmpty();
    }



    /**
     * When 존재 하지 않는 역으로 지하철 노선 생성을 요청
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 - 실패")
    @Test
    void createLine_fail() {
        //given
        String 없는역 = "5";

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // when
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(
                신분당선, 빨강색
                , "100", 없는역, downStationId);


        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("존재하지 않는 역 역니다.");
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
        //given
        String 상행역 = "뚝섬역";
        String upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // given
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";
        지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);


        //given
        상행역 = "삼성역";
        upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        하행역 = "합정역";
        downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // given
        String _2호선 = "2호선";
        String 연두색 = "bg-green-600";
        지하철_노선_생성_요청(_2호선, 연두색, "100", upStationId, downStationId);


        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("lines")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsExactly(신분당선, _2호선);
        assertThat(response.jsonPath().getList("color")).containsExactly(빨강색, 연두색);
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
        String 상행역 = "뚝섬역";
        String upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // given
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";
        ExtractableResponse<Response> response
                = 지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);

        // when
        response = RestAssured
                .given().log().all()
                .when().get(response.header("location"))
                .then().log().all().extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선);
        assertThat(response.jsonPath().getString("color")).isEqualTo(빨강색);

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
        String 상행역 = "뚝섬역";
        String upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // given
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";
        ExtractableResponse<Response> response
                = 지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);

        // when
        Map<String, String> modifyParams = getParamsLineMap("구분당선","bg-blue-600");
        String location = response.header("location");

        response = RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .body(modifyParams)
                .when().put(location)
                .then().log().all().extract();
                //.header("content-length",45)
                //질문 : 해당 내용을 추가하면 ClientProtocolException : Content-Length header already present로 Error가 생기는 이유가 뭘까요?

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
        String 상행역 = "뚝섬역";
        String upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // given
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";
        ExtractableResponse<Response> response
                = 지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);

        // when
        String location = response.header("location");

        response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     *  Given 지하철노선 생성을 요청 하고
     *  When 같은 이름으로 지하철 노선 생성을 요청 하면
     *  Then 지하철 노선역 생성이 실패한다.
     */
    @Test
    @DisplayName("지하철노선 중복 이름으로 생성 불가")
    void duplicated_line_interdict() {
        //given
        String 상행역 = "뚝섬역";
        String upStationId = 지하철역_생성_요청(상행역).jsonPath().getString("id");

        //given
        String 하행역 = "신도림역";
        String downStationId = 지하철역_생성_요청(하행역).jsonPath().getString("id");

        // given
        String 신분당선 = "신분당선";
        String 빨강색 = "bg-red-600";
        ExtractableResponse<Response> response
                = 지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);

        //when
        response = 지하철_노선_생성_요청(신분당선, 빨강색, "100", upStationId, downStationId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("중복된 라인을 생성할 수 없습니다.");
    }

}
