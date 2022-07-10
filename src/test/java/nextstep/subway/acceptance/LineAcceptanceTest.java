package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private StationAcceptanceTest stationAcceptanceTest;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        stationAcceptanceTest = new StationAcceptanceTest();
    }

    public ExtractableResponse<Response> createLine(String lineName, String upStationName, String downStationName, String color, int distance) {
        Long upStationId = stationAcceptanceTest.createStation(upStationName)
                .jsonPath()
                .getLong("id");

        Long downStationId = stationAcceptanceTest.createStation(downStationName)
                .jsonPath()
                .getLong("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        //when(지하철 노선을 생성한다.)
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params).post("/lines")
                .then().log().all()
                .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse;
    }

    public ExtractableResponse<Response> gets() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public ExtractableResponse<Response> get(long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        //given(상행역, 하행역 지하철역을 생성한다.)
        final String 시청역 = "시청역";
        final String 강남역 = "강남역";
        final String 신분당선 = "신분당선";

        createLine(신분당선, 시청역, 강남역, "bg-red-600", 10);

        //then(지하철 노선 목록과 앞서 생성한 지하철 노선을 비교하여 생성된 지하철이 존재하는지 확인한다.)
        ExtractableResponse<Response> getsResponse = gets();

        //상태코드 200 확인
        assertThat(getsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        //지하철 노선명 확인
        assertThat(getsResponse.body().jsonPath().getList("name", String.class)).contains(신분당선);

        //지하철 노선의 상행, 하행역 확인
        //TODO : json path element 이해가 가지 않음[공부 필요]
        assertThat(getsResponse.body().jsonPath().getList("stations[0].name", String.class)).containsExactly(시청역, 강남역);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {

        //given(2개 이상의 지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";

        final String 일호선 = "일호선";
        final String 구로역 = "구로역";
        final String 신도림역 = "신도림역";

        ExtractableResponse<Response> lineResponseOne = createLine(신분당선, 시청역, 강남역, "bg-red-600", 10);
        ExtractableResponse<Response> lineResponseTwo = createLine(일호선, 구로역, 신도림역, "bg-red-200", 15);

        //when
        ExtractableResponse<Response> getResponse = gets();

        //then
        //상태코드 200 확인
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        //앞서 생성한 지하철 노선이 지하철 노선 목록 중에 존재하는지 확인
        assertThat(getResponse.jsonPath().getList("name", String.class)).contains(신분당선, 일호선);
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {

        //given(지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String color = "bg-red-600";

        long lineId = createLine(신분당선, 시청역, 강남역, color, 10).jsonPath().getLong("id");

        //when(생성한 지하철 노선을 조회한다.)
        ExtractableResponse<Response> getResponse = get(lineId);

        //then
        //지하철 노선 id 확인
        assertThat(getResponse.jsonPath().getLong("id")).isEqualTo(lineId);

        //지하철 노선명 확인
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(신분당선);
        

    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {

        //given(지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String 일호선 = "일호선";
        final String color = "bg-red-600";
        final String updateColor = "bg-red-700";

        long lineId = createLine(신분당선, 시청역, 강남역, color, 10).jsonPath().getLong("id");

        //when(생성한 지하철 노선을 수정한다.)
        Map<String, Object> params = new HashMap<>();
        params.put("name", 일호선);
        params.put("color", updateColor);

        ExtractableResponse<Response> updateResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params).put("/lines/" + lineId)
                .then().log().all()
                .extract();

        //then(수정된 지하철 노선을 확인한다.)
        
        //상태코드 200 확인
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> getResponse = get(lineId);

        //수정된 지하철 노선명 확인
        assertThat(getResponse.jsonPath().getString("name")).isEqualTo(일호선);

        //수정된 지하철 노선 색 확인
        assertThat(getResponse.jsonPath().getString("color")).isEqualTo(updateColor);

    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {

        //given(지하철 노선을 생성한다.)
        final String 신분당선 = "신분당선";
        final String 강남역 = "강남역";
        final String 시청역 = "시청역";
        final String color = "bg-red-600";

        long lineId = createLine(신분당선, 시청역, 강남역, color, 10).jsonPath().getLong("id");

        //when(생성한 지하철 노선을 삭제한다.)
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();

        //then(삭제가 되었는지 확인한다.)
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
