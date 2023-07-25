package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineModifyRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DirtiesContext
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        long upStationId = createStation("강남역").jsonPath().getLong("id");
        long downStationId = createStation("범계역").jsonPath().getLong("id");

        // when
        LineRequest req = new LineRequest("분당선", "red", upStationId, downStationId, 10);
        createLine(req);

        //then
        List<String> lines = getAllLines().jsonPath().getList("name", String.class);

        assertThat(lines).containsAnyOf("분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DirtiesContext
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void readLines() {

        //given
        createSampleLine("강남역", "범계역", "분당선", "yellow", 10);
        createSampleLine("석계역", "태릉입구역", "6호선", "brown", 10);

        //when
        List<String> lines = getAllLines().jsonPath().getList("name", String.class);

        //then
        assertThat(lines).containsAnyOf("분당선");
        assertThat(lines).containsAnyOf("6호선");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DirtiesContext
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void readLine() {

        //given
        long id = createSampleLine("강남역", "범계역", "분당선", "yellow", 10);

        //when
        LineResponse response = getLine(id).jsonPath().getObject("", LineResponse.class);

        //then
        assertThat(response.getName()).isEqualTo("분당선");
        assertThat(response.getColor()).isEqualTo("yellow");
        assertThat(response.getStations().get(0).getName()).isEqualTo("강남역");


    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DirtiesContext
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifyLine() {

        //given
        long id = createSampleLine("강남역", "범계역", "분당선", "yellow", 10);

        //when
        LineModifyRequest req2 = new LineModifyRequest("신분당선", "red");
        LineResponse response = modifyLine(id, req2).jsonPath().getObject("", LineResponse.class);

        //then
        assertThat(response.getName()).isEqualTo("신분당선");
        assertThat(response.getColor()).isEqualTo("red");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DirtiesContext
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        //given
        long id = createSampleLine("강남역", "범계역", "분당선", "yellow", 10);

        //when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/"+id);

        //then
        assertThat(getAllLines().jsonPath().getList("name")).doesNotContain("분당선");
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "명동역");
//
//        long id = getDefaultRestAssured()
//                .body(params).post("/stations").jsonPath().getLong("id");
//
//        //when
//        getDefaultRestAssured().delete("/stations/"+id).then().statusCode(HttpStatus.NO_CONTENT.value());
//
//        //then
//        List<String> list = getDefaultRestAssured()
//                .get("/stations").jsonPath().getList("name", String.class);
//
//        assertThat(list).doesNotContain("명동역");
    }

    private Long createSampleLine(String upStationName, String downStationName, String lineName, String lineColor, int distance){
        long upStationId1 = createStation(upStationName).jsonPath().getLong("id");
        long downStationId1 = createStation(downStationName).jsonPath().getLong("id");
        LineRequest req1 = new LineRequest(lineName, lineColor, upStationId1, downStationId1, distance);
        return createLine(req1).jsonPath().getLong("id");

    }
    private Response createLine(LineRequest lineRequest){

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().body(lineRequest)
                .post("/lines");
    }

    private Response createStation(String name){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().body(params)
                .post("/stations");
    }

    private Response getAllLines(){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines");
    }

    private Response getLine(Long id){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/"+id);
    }

    private Response modifyLine(Long id, LineModifyRequest req){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(req)
                .put("/lines/"+id);
    }


}