package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.helper.LineTestHelper;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:data/station-init.sql"})
public class LineAcceptanceTest {

    final String 신분당선 = "신분당선";
    final String 분당선 = "분당선";
    final String 빨간색 = "bg-red-600";
    final String 노란색 = "bg-yellow-600";
    final Long 청계산입구역_ID = 1L;
    final Long 판교역_ID = 2L;
    final Long 선릉역_ID = 3L;
    final String 청계산입구역 = "청계산입구역";
    final String 판교역 = "판교역";
    final String 선릉역 = "선릉역";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    public void createStationLine(){
        //when
        ExtractableResponse<Response> createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);
        //then
        ExtractableResponse<Response> getResponse = 지하철노선_조회(createResponse);
        지하철노선_검증(createResponse, getResponse);
    }

    private ExtractableResponse<Response> 지하철노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance){
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("lines")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 지하철노선_조회(ExtractableResponse<Response> response){
        return RestAssured.given().log().all()
                .when().get("/lines" + "/"+ response.jsonPath().getLong("id"))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_삭제(ExtractableResponse<Response> response){
        return RestAssured.given().log().all()
                .when().delete("/lines" + "/"+ response.jsonPath().getLong("id"))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_수정(ExtractableResponse<Response> response, String name, String color){
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured.given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines" + "/"+ response.jsonPath().getLong("id"))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선_조회(){
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private void 지하철노선_검증(ExtractableResponse<Response> createResponse, ExtractableResponse<Response> getResponse){
        assertThat(LineTestHelper.getId(createResponse)).isEqualTo(LineTestHelper.getId(getResponse));
        assertThat(LineTestHelper.getColor(createResponse)).isEqualTo(LineTestHelper.getColor(getResponse));
        assertThat(LineTestHelper.getName(createResponse)).isEqualTo(LineTestHelper.getName(getResponse));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    public void viewStationLineList(){
        //given
        지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);
        지하철노선_생성(분당선, 노란색, 청계산입구역_ID, 선릉역_ID, 10);

        //when
        ExtractableResponse<Response> getResponse = 지하철노선_조회();

        //then
        assertThat(getResponse.jsonPath().getList("name", String.class)).containsAnyOf(신분당선);
        assertThat(getResponse.jsonPath().getList("color", String.class)).containsAnyOf(빨간색);
        assertThat(getResponse.jsonPath().getList("name", String.class)).containsAnyOf(분당선);
        assertThat(getResponse.jsonPath().getList("color", String.class)).containsAnyOf(노란색);
    };

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철노선 조회")
    @Test
    public void viewSingleStationLine(){
        //given
        ExtractableResponse<Response> createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);

        //when
        ExtractableResponse<Response> getResponse = 지하철노선_조회(createResponse);

        //then
        지하철노선_검증(createResponse, getResponse);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선의 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    public void updateStationLine(){
        //given
        ExtractableResponse<Response> createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);

        //when
        final String 수내역 = "수내역";
        final String 파란색 = "bg-blue-600";
        지하철노선_수정(createResponse, 수내역, 파란색);

        //then
        ExtractableResponse<Response> changeResponse = 지하철노선_조회(createResponse);
        assertThat(changeResponse.jsonPath().getString("name")).isEqualTo(수내역);
        assertThat(changeResponse.jsonPath().getString("color")).isEqualTo(파란색);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    public void removeStationLine(){
        //given
        ExtractableResponse<Response> createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);

        //when
        지하철노선_삭제(createResponse);

        //then
        ExtractableResponse<Response> getResponse = 지하철노선_조회();
        assertThat(getResponse.jsonPath().getList("name", String.class)).doesNotContain(신분당선);
        assertThat(getResponse.jsonPath().getList("color", String.class)).doesNotContain(빨간색);
    }
}
