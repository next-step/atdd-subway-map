package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.exception.ApiError;
import subway.common.exception.ErrorMessage;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    Map<String, Long> stationIdMap;
    String[] stationNames = {"지하철역", "새로운지하철역"};

    @BeforeEach
    void 지하철역_셋팅() {
        stationIdMap = new HashMap<String, Long>();
        for (String name : stationNames) {
            Object id = 지하철역_생성(name).jsonPath().get("id");
            stationIdMap.put(name, Long.valueOf(id.toString()));
        }
    }

    //    지하철노선 생성
//    When 지하철 노선을 생성하면
//    Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철노선 생성")
    @Test
    void makeLine() {
        // given 지하철 노선

        //when
        LineResponse result = 라인생성("1호선", "RED", stationIdMap.get(stationNames[0]), stationIdMap.get(stationNames[1]), 10);

        //then
        assertThat(result.getName()).isEqualTo("1호선");
        assertThat(result.getColor()).isEqualTo("RED");
        List<StationResponse> stations = result.getStations();

        assertThat(stations.get(0).getId()).isEqualTo(stationIdMap.get(stationNames[0]));
        assertThat(stations.get(1).getId()).isEqualTo(stationIdMap.get(stationNames[1]));
    }


    //
//    지하철노선 목록 조회
//
//    Given 2개의 지하철 노선을 생성하고
//    When 지하철 노선 목록을 조회하면
//    Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    @DisplayName("지하철노선 목록 조회")
    @Test
    void searchLines() {
        // given
        라인생성( "1호선", "RED", stationIdMap.get(stationNames[0]),stationIdMap.get(stationNames[1]), 10);
        라인생성("2호선", "YELLOW", stationIdMap.get(stationNames[0]), stationIdMap.get(stationNames[1]), 10);

        //when
        List<LineResponse> list = 전체라인조회().jsonPath().getList("", LineResponse.class);

        //then
        assertThat(list.size()).isEqualTo(2);
    }

    //
//            지하철노선 조회
//
//    Given 지하철 노선을 생성하고
//    When 생성한 지하철 노선을 조회하면
//    Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    @DisplayName("지하철노선 조회")
    @Test
    void searchStationDetail(){

        Long id = 라인생성("1호선", "RED", stationIdMap.get(stationNames[0]), stationIdMap.get(stationNames[1]), 10).getId();

        LineResponse result = 라인조회(id).jsonPath().getObject("", LineResponse.class);

        assertThat(result.getName()).isEqualTo("1호선");
        assertThat(result.getColor()).isEqualTo("RED");

        StationResponse upStation = result.getStations().get(0);
        StationResponse downStation = result.getStations().get(1);
        assertThat(upStation.getName()).isEqualTo(stationNames[0]);
        assertThat(downStation.getName()).isEqualTo(stationNames[1]);
    }

    private ExtractableResponse<Response> 라인조회(long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
//
//    지하철노선 수정
//
//* Given 지하철 노선을 생성하고
//* When 생성한 지하철 노선을 수정하면
//* Then 해당 지하철 노선 정보는 수정된다
    @DisplayName("지하철노선 수정")
    @Test
    void modifyStation(){

        Long id = 라인생성("1호선", "RED", stationIdMap.get(stationNames[0]), stationIdMap.get(stationNames[1]), 10)
                        .getId();

        Map<String,String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "YELLOW");

        LineResponse result = 라인수정(id, params).jsonPath().getObject("", LineResponse.class);

        assertThat(result.getName()).isEqualTo("2호선");
        assertThat(result.getColor()).isEqualTo("YELLOW");
    }



    private ExtractableResponse<Response> 라인수정(Long id, Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
//
//    지하철노선 삭제
//    Given 지하철 노선을 생성하고
//    When 생성한 지하철 노선을 삭제하면
//    Then 해당 지하철 노선 정보는 삭제된다

    @DisplayName("지하철노선 삭제")
    @Test
    void deleteStation(){
        String name = "1호선";
        String color = "RED";
        Long upStationId = stationIdMap.get(stationNames[0]);
        Long downStationId = stationIdMap.get(stationNames[1]);
        int distance = 10;

        Long id = 라인생성(name, color, upStationId, downStationId, distance).getId();

        ExtractableResponse<Response> result = 라인삭제(id);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> 라인조회결과 = 라인조회(id);
        assertThat(라인조회결과.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(라인조회결과.body().as(ApiError.class).getMessage())
                .isEqualTo(ErrorMessage.NOT_FOUND_LINE.getMessage());

    }

    private LineResponse 라인생성(String name, String color, Long upStationId, Long downStationId, int distance){
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        return 라인생성(lineRequest).as(LineResponse.class);
    }
    private ExtractableResponse<Response> 라인삭제(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
    }
    private ExtractableResponse<Response> 라인생성(LineRequest req) {
        Map<String, Object> params = req.getParams();

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 전체라인조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

}
