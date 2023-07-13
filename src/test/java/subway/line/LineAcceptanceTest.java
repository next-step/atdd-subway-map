package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    Map<String, Integer> stationIdMap;
    String[] stationNames = {"지하철역", "새로운지하철역"};

    @BeforeEach
    void 지하철역_셋팅() {
        stationIdMap = new HashMap<String, Integer>();
        for (String name : stationNames) {
            stationIdMap.put(name, 지하철역_생성(name).jsonPath().get("id"));
        }

    }

    //    지하철노선 생성
//    When 지하철 노선을 생성하면
//    Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철노선 생성")
    @Test
    void makeLine() {
        // given 지하철 노선
        String name = "1호선";
        String color = "RED";
        int upStationId = 1;
        int downStationId = 2;
        int distance = 10;

        //when
        LineResponse result = 라인생성(name, color, 1, 1, distance);

        //then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getColor()).isEqualTo(color);
        assertThat(result.getUpstationId()).isEqualTo(upStationId);
        assertThat(result.getDownStationId()).isEqualTo(downStationId);
        assertThat(result.getDistance()).isEqualTo(distance);
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
        String name = "1호선";
        String color = "RED";
        int upStationId = 1;
        int downStationId = 2;
        int distance = 10;

        //when

        String name2 = "2호선";
        String color2 = "YELLOW";
        int upStationId2 = 1;
        int downStationId2 = 2;
        int distance2 = 10;

        라인생성(name, color, upStationId, downStationId, distance);
        라인생성(name2, color2, upStationId2, downStationId2, distance2);

        List<LineResponse> list = 전체라인조회().jsonPath().getList("*", LineResponse.class);

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
        String name = "1호선";
        String color = "RED";
        int upStationId = 1;
        int downStationId = 2;
        int distance = 10;

        int id = 라인생성(name, color, upStationId, downStationId, distance).getId();

        LineRequest result = 라인조회(id);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getColor()).isEqualTo(color);
        assertThat(result.getDistance()).isEqualTo(distance);
    }

    private ExtractableResponse<Response> 라인조회(long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line/{id}", id)
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
        String name = "1호선";
        String color = "RED";
        int upStationId = 1;
        int downStationId = 2;
        int distance = 10;

        int id = 라인생성(name, color, upStationId, downStationId, distance).getId();

        String modifyName = "2호선";
        String modifyColor = "YELLOW";
        Map<String,String> params = new HashMap<>();
        params.put("name", modifyName);
        params.put("color", modifyColor);

        LineRequest result = 라인수정(id, params);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getColor()).isEqualTo(color);
        assertThat(result.getDistance()).isEqualTo(distance);
    }



    private LineRequest 라인수정(int id, Map<String, String> params) {
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
        int upStationId = 1;
        int downStationId = 2;
        int distance = 10;

        int id = 라인생성(name, color, upStationId, downStationId, distance).getId();

        ExtractableResponse<Response> result = 라인조회(id);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private LineResponse 라인생성(String name, String color, int upStationId, int downStationId, int distance){
        LineRequest lineRequest = new LineReqeust(name, color, upStationId, downStationId, distance);
        return 라인생성(lineRequest).jsonPath().get("*", LineResponse.class);
    }
    private ExtractableResponse<Response> 라인생성(LineRequest req) {
        Map<String, String> params = req.getParams();
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
                .when().get("/stations")
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
