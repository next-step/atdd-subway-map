package subway.domain;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.common.SetupTest.분당선_노선을_생성한다;
import static subway.common.SetupTest.신분당선_노선을_생성한다;

@DisplayName("지하철 노선을 관리한다.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/setup-station.sql")
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");
        param.put("upStationId", 1);
        param.put("downStationId", 2);
        param.put("distance", 10);

        //when
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat((String) response.jsonPath().get("name")).isEqualTo("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showStationLinesList(){
        //given
        신분당선_노선을_생성한다();
        분당선_노선을_생성한다();

        //when
        var response = 지하철노선_목록을_조회한다();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("특정 지하철노선을 조회한다.")
    @Test
    void showStationLineById(){
        //given
        신분당선_노선을_생성한다();
        분당선_노선을_생성한다();

        //when
        var response = 지하철노선을_조회한다(2);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((String)response.jsonPath().get("name")).isEqualTo("분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("특정 지하철노선을 수정한다.")
    @Test
    void updateStationLine(){
        //given
        신분당선_노선을_생성한다();

        //when
        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("name", "수정된 이름");
        updateParam.put("color", "bg-black-600");

        int id = (Integer) 지하철노선_목록을_조회한다().jsonPath().getList("id").get(0);

        var response = 지하철노선을_수정한다(id, updateParam);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((String) 지하철노선을_조회한다(id).jsonPath().get("name")).isEqualTo(updateParam.get("name"));
        assertThat((String) 지하철노선을_조회한다(id).jsonPath().get("color")).isEqualTo(updateParam.get("color"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("특정 지하철노선을 삭제한다.")
    @Test
    void deleteStationLineById(){
        String deleteLineName = "분당선";

        신분당선_노선을_생성한다();
        분당선_노선을_생성한다();

        ArrayList<Map<String, Object>> list = 지하철노선_목록을_조회한다().body().as(ArrayList.class);
        Map<String, Object> deleteLine = list.stream().filter(item -> item.get("name").equals(deleteLineName))
                .findFirst()
                .orElse(null);

        var response = 지하철노선을_삭제한다((Integer) deleteLine.get("id"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(지하철노선_목록을_조회한다().jsonPath().getList("name").contains(deleteLineName)).isFalse();
    }

    private ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_조회한다(int id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}",id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_수정한다(int id, Map<String, String> param1) {
        return RestAssured.given().log().all()
                .body(param1)
                .contentType(ContentType.JSON)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_삭제한다(int id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}",id)
                .then().log().all()
                .extract();
    }
}
