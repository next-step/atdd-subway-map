package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class LineAcceptanceTest extends AcceptanceTest{
    StationTestMethod stationTestMethod;
    public String stationId1;
    public String stationId2;
    public String stationId3;
    public String stationId4;

    @BeforeEach
    public void lineTestSetUp() {
        stationId1 = stationTestMethod.지하철역_생성("구일역").jsonPath().getString("id");
        stationId2 = stationTestMethod.지하철역_생성("구로역").jsonPath().getString("id");;
        stationId3 = stationTestMethod.지하철역_생성("신도립역").jsonPath().getString("id");;
        stationId4 = stationTestMethod.지하철역_생성("문래역").jsonPath().getString("id");;
    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @Test
    @DisplayName("지하철노선을 생성한다")
    void createLine(){
        ExtractableResponse<Response> response =
                지하철노선_생성("파랑선", stationId1, stationId2, "blue", "10");
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getLong("id")).isNotNull()
        );

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록 조회")
    void getLineList(){
        지하철노선_생성("파랑선", stationId1, stationId2, "blue", "10");
        지하철노선_생성("초록선", stationId3, stationId4, "green", "10");

        ExtractableResponse<Response> response = 지하철노선_목록조회();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("name")).contains("초록선", "파랑선")
        );

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 시하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 조회")
    void getLine(){
        지하철노선_생성("파랑선", stationId1, stationId2, "blue", "10");
        Long lineId = 지하철노선_생성("초록선", stationId3, stationId4, "green", "10")
                .jsonPath().getLong("id");

        ExtractableResponse<Response> response = 지하철노선_단일조회(lineId);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("초록선")
        );

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @Test
    @DisplayName("지하철조선 수정")
    void modifyLine(){
        Long lineId = 지하철노선_생성("초록선", stationId3, stationId4, "green", "10")
                .jsonPath().getLong("id");
        Map<String, String> params = new HashMap<>();
        params.put("name", "노랑선");
        params.put("color", "yellow");

        지하철노선_수정(params, lineId);

        ExtractableResponse<Response> response = 지하철노선_단일조회(lineId);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("노랑선"),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo("yellow")
        );

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @Test
    @DisplayName("지하철노선 삭제")
    void deleteLine(){
        지하철노선_생성("파랑선", stationId1, stationId2, "blue", "10");
        Long lineId = 지하철노선_생성("초록선", stationId3, stationId4, "green", "10")
                .jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철노선_삭제(lineId);


        List<String> lineName = 지하철노선_목록조회().jsonPath().getList("name");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineName).doesNotContain("초록선")
        );
    }

    ExtractableResponse<Response> 지하철노선_생성(String lineName, String upStationId, String downStationId,
                                           String color, String distance){
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
                .given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then().log().all()
                .extract()
                ;
    }

    ExtractableResponse<Response> 지하철노선_목록조회(){
        return RestAssured
                        .given().log().all()
                        .when()
                        .get("/lines")
                        .then().log().all()
                        .extract()
                ;
    }

    ExtractableResponse<Response> 지하철노선_단일조회(Long lineId){
        return RestAssured
                        .given().log().all()
                        .pathParam("lineId", lineId)
                        .when()
                        .get("/lines/{lineId}")
                        .then().log().all()
                        .extract()
                ;
    }

    void 지하철노선_수정(Map<String , String> params, Long lineId){
        RestAssured
                .given().log().all()
                .body(params)
                .pathParam("lineId", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{lineId}")
                .then().log().all()
                .extract()
        ;
    }

    ExtractableResponse<Response> 지하철노선_삭제(Long lineId){
        return RestAssured
                .given().log().all()
                .pathParam("lineId", lineId)
                .when()
                .delete("/lines/{lineId}")
                .then().log().all()
                .extract()
        ;
    }
}
