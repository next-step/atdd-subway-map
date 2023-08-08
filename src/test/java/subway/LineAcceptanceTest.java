package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.SubwayUtils.*;

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
    void createLineSuccess() {
        // given
        long upStationId = createStation("강남역").jsonPath().getLong("id");
        long downStationId = createStation("범계역").jsonPath().getLong("id");

        // when
        createLine(createLineRequest("분당선", "red", upStationId, downStationId, 10));

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
        assertThat(lines).containsExactlyInAnyOrder("분당선", "6호선");

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
        assertThat(response.getStations().stream().map(StationResponse::getName)).containsExactlyInAnyOrder("강남역", "범계역");


    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DirtiesContext
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifyLineSuccess() {

        //given
        long id = createSampleLine("강남역", "범계역", "분당선", "yellow", 10);

        //when
        LineResponse response = modifyLine(id, createModifyRequest("신분당선", "red")).jsonPath().getObject("", LineResponse.class);

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

    }

    private Long createSampleLine(String upStationName, String downStationName, String lineName, String lineColor, int distance){
        long upStationId1 = createStation(upStationName).jsonPath().getLong("id");
        long downStationId1 = createStation(downStationName).jsonPath().getLong("id");
        return createLine(createLineRequest(lineName, lineColor, upStationId1, downStationId1, distance)).jsonPath().getLong("id");

    }

}