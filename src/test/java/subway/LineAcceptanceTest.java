package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.web.dto.LineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    private static final int LENGTH_TWO = 2;
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 => 지하철 노선 목록의 이름 중에 1호선이 있다.
     */
    @Test
    void 지하철_노선_생성_후_목록_조회시_찾을_수_있다() {
        //when
        String lineName = "신분당선";
        String lineColor = "bg-red-600";
        Long upStationId = 1L;
        Long downStationId = 2L;
        Long distance = 10L;

        final Map<String, String> stationParams = new HashMap<>();
        stationParams.put("name", "지하철역");
        getStationResponse(stationParams);

        stationParams.put("name", "새로운지하철역");
        getStationResponse(stationParams);

        final Map<String, String> lineParams = new HashMap<>();
        putParams(lineParams, lineName, lineColor, upStationId, downStationId, distance);

        getSaveLineResponse(lineParams);

        //then
        ExtractableResponse<Response> readResponses = getReadResponses();

        Assertions.assertThat(readResponses.body().jsonPath().getList("name")).contains(lineName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다. => 지하철 노선 목록 조회시 응답받은 List<Response>의 길이가 2이다.
     */
    @Test
    void 두_개의_지하철_노선_생성_후_목록_조회시_응답_받은_Response의_길이는_2이다() {
        //given
        String lineTwoName = "2호선";
        String lineTwoColor = "초록색";

        Map<String, String> params = new HashMap<>();
//        putParams(params, lineTwoName, lineTwoColor);
        getSaveLineResponse(params);

        String lineThreeName = "3호선";
        String lineThreeColor = "주황색";

//        putParams(params, lineThreeName, lineThreeColor);
        getSaveLineResponse(params);

        //when
        ExtractableResponse<Response> readResponse = getReadResponses();

        //then
        List list = readResponse.body().jsonPath().get();

        Assertions.assertThat(list.size()).isEqualTo(LENGTH_TWO);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_생성_후_생성된_노선으로_조회시_노선의_정보를_알_수_있다() {
        //given
        String lineFourName = "4호선";
        String lineFourColor = "하늘색";

        Map<String, String> params = new HashMap<>();
//        putParams(params, lineFourName, lineFourColor);
        Long id = getSaveLineResponse(params).getId();

        String lineFiveName = "5호선";
        String lineFiveColor = "보라색";
//        putParams(params, lineFiveName, lineFiveColor);
        getSaveLineResponse(params);

        //when
        ExtractableResponse<Response> findResponse = getFindResponse(id);

        //then
        String lineName = findResponse.body().jsonPath().get("name");
        String lineColor = findResponse.body().jsonPath().get("color");

        Assertions.assertThat(lineName).isEqualTo(lineFourName);
        Assertions.assertThat(lineColor).isEqualTo(lineFourColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_생성하고_수정하면_노선_정보는_수정된다() {
        //given
        String lineSixName = "6호선";
        String lineSixColor = "갈색";

        Map<String, String> params = new HashMap<>();
//        putParams(params, lineSixName, lineSixColor);
        Long id = getSaveLineResponse(params).getId();

        //when
        String lineChangeColor = "흑색";
        params.put("color", lineChangeColor);

        ExtractableResponse<Response> updateResponse = getUpdateResponse(params, id);

        //then
        String color = updateResponse.body().jsonPath().get("color");

        Assertions.assertThat(color).isEqualTo(lineChangeColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_생성하고_삭제하면_노선_정보는_삭제_된다() {
        //given
        String lineSevenName = "7호선";
        String lineSevenColor = "금색";

        Map<String, String> params = new HashMap<>();
//        putParams(params, lineSevenName, lineSevenColor);
        Long id = getSaveLineResponse(params).getId();

        //when
        ExtractableResponse<Response> deleteResponse = getDeleteResponse(id);

        // then
        Assertions.assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void getStationResponse(Map<String, String> stationParams) {
        ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .body(stationParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract();

        Assertions.assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> getDeleteResponse(Long id) {
        return RestAssured
                .given()
                .when()
                .delete("/lines/" + id)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> getUpdateResponse(Map<String, String> params, Long id) {
        ExtractableResponse<Response> updateResponse = RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();

        Assertions.assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        return updateResponse;
    }

    private ExtractableResponse<Response> getFindResponse(Long id) {
        return RestAssured
                .given()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getReadResponses() {
        ExtractableResponse<Response> readResponse = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        Assertions.assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return readResponse;

    }

    private void putParams(Map<String, String> params, String lineName, String lineColor, Long upStationId, Long downStationId, Long distance) {
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
    }

    private LineResponse getSaveLineResponse(Map<String, String> params) {
        ExtractableResponse<Response> saveResponse = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Assertions.assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return saveResponse.jsonPath().getObject("", LineResponse.class);
    }
}
