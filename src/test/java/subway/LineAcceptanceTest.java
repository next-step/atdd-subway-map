package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;

import subway.domain.LineResponse;
import subway.domain.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends BaseAcceptanceTest{


    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
        Map<String, String> param1 = Map.of("name", "역삼역");
        Map<String, String> param2 = Map.of("name", "선릉역");
        Map<String, String> param3 = Map.of("name", "왕십리역");
        Map<String, String> param4 = Map.of("name", "고색역");

        createStation(param1);
        createStation(param2);
        createStation(param3);
        createStation(param4);
    }

    @DisplayName("지하철 노선을 생성하면 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.")
    @Test
    void test_지하철노선_생성() {
        //when
        LineResponse linePostResponse = given()
            .body(getRequestParam_신분당선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all().extract().jsonPath().getObject(".", LineResponse.class);
        List<StationResponse> stationPostResponses = linePostResponse.getStations();

        //then
        LineResponse lineGetResponse = when().get("/lines").then().extract().jsonPath().getList(".", LineResponse.class).get(0);
        List<StationResponse> stationsResponse = lineGetResponse.getStations();
        assertAll(
            () -> assertThat(lineGetResponse).extracting(LineResponse::getName).isEqualTo(linePostResponse.getName()),
            () -> assertThat(lineGetResponse).extracting(LineResponse::getColor).isEqualTo(linePostResponse.getColor()),
            () -> assertThat(stationsResponse).extracting(StationResponse::getId).isEqualTo(stationPostResponses.stream().map(StationResponse::getId).collect(Collectors.toList())),
            () -> assertThat(stationsResponse).extracting(StationResponse::getName).containsExactly("역삼역", "선릉역")
        );
    }

    @DisplayName("2개의 지하철 노선을 생성하고 지하철 노선 목록을 조회하면 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.")
    @Test
    void test_지하철_노선_목록_조회() {
        //given
        given()
            .body(getRequestParam_신분당선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all();

        given()
            .body(getRequestParam_분당선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all();

        //when
        List<LineResponse> lineResponses = when().get("/lines").then().extract().jsonPath().getList(".", LineResponse.class);
        assertAll(
            () -> assertThat(lineResponses).hasSize(2),
            () -> assertThat(lineResponses).extracting(LineResponse::getName).containsExactly(getRequestParam_신분당선().get("name"), getRequestParam_분당선().get("name")),
            () -> assertThat(lineResponses).extracting(LineResponse::getColor).containsExactly(getRequestParam_신분당선().get("color"), getRequestParam_분당선().get("color"))
        );
    }

    @DisplayName("지하철 노선을 생성하고 생성한 지하철 노선을 조회하면 생성한 지하철 노선의 정보를 응답받을 수 있다.")
    @Test
    void test_지하철_생성_노선_조회() {
        //given
        LineResponse linePostResponse = given()
            .body(getRequestParam_신분당선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all().extract().jsonPath().getObject(".", LineResponse.class);

        //when
        LineResponse lineResponse_신분당선 = when().get("/lines/" + linePostResponse.getId()).then().extract().jsonPath().getObject(".", LineResponse.class);
        assertAll(
            () -> assertThat(lineResponse_신분당선.getId()).isEqualTo(1),
            () -> assertThat(lineResponse_신분당선.getName()).isEqualTo(getRequestParam_신분당선().get("name")),
            () -> assertThat(lineResponse_신분당선.getColor()).isEqualTo(getRequestParam_신분당선().get("color"))
        );
    }

    @DisplayName("지하철 노선을 생성하고 생성한 지하철 노선을 수정하면 해당 지하철 노선 정보는 수정된다.")
    @Test
    void test_지하철_노선_수정() {
        //given
        LineResponse linePostResponse = given()
            .body(getRequestParam_신분당선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all().extract().jsonPath().getObject(".", LineResponse.class);
        Map<String, String> putRequest = Map.of(
            "name", "다른분당선",
            "color", "Red"
        );
        //when
        given()
            .body(putRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/lines/" + linePostResponse.getId())
            .then()
            .log().all().statusCode(HttpStatus.SC_OK);

        //then
        LineResponse lineResponse_신분당선_수정 = when().get("/lines/" + linePostResponse.getId()).then().extract().jsonPath().getObject(".", LineResponse.class);
        assertThat(lineResponse_신분당선_수정.getColor()).isEqualTo(putRequest.get("color"));
        assertThat(lineResponse_신분당선_수정.getName()).isEqualTo(putRequest.get("name"));
    }

    @DisplayName("지하철 노선을 생성하고 생성한 지하철 노선을 삭제하면 해당 지하철 노선 정보는 삭제된다.")
    @Test
    void test_지하철_노선_삭제() {
        //given
        LineResponse linePostResponse = given()
            .body(getRequestParam_신분당선())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines").then().log().all().extract().jsonPath().getObject(".", LineResponse.class);

        //when & then
        when()
            .delete("/lines/" + linePostResponse.getId())
            .then()
            .log().all().statusCode(HttpStatus.SC_NO_CONTENT);
        when().get("/lines/" + linePostResponse.getId()).then().log().all().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    private Map<String, String> getRequestParam_신분당선() {
        String lineName = "신분당선";
        String lineColor = "bg-red-600";
        long upStationId = 1L;
        long downStationId = 2L;
        Integer distance = 10;

        return Map.of(
            "name", lineName,
            "color", lineColor,
            "upStationId", Long.toString(upStationId),
            "downStationId", Long.toString(downStationId),
            "distance", distance.toString()
        );
    }

    private Map<String, String> getRequestParam_분당선() {
        String lineName = "분당선";
        String lineColor = "bg-yellow-600";
        Long upStationId = 3L;
        Long downStationId = 4L;
        Integer distance = 2;

        return Map.of(
            "name", lineName,
            "color", lineColor,
            "upStationId", upStationId.toString(),
            "downStationId", downStationId.toString(),
            "distance", distance.toString()
        );
    }

}
