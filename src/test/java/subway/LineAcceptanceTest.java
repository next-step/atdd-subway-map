package subway;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 기능 테스트")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
    * When 지하철 노선을 생성하면
    * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    */

    @DisplayName("지하철노선 생성")
    @Test
    void createLine(){
        generateStations(new String[]{"왕십리역", "마장역"});

        Map<String, String> params = generateParams("신분당선", "bg-red-600", "1", "2", "10");

        //when
        ExtractableResponse<Response> response = LineUtils.createLine(params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        List<String> lineNames = LineUtils.getLineNames();
        assertThat(lineNames).containsAnyOf("신분당선");
    }


    /** Given 2개의 지하철 노선을 생성하고
    *When 지하철 노선 목록을 조회하면
    *Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    */

    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines(){
        generateStations(new String[]{"왕십리역", "마장역", "동작역"});

        //given
        Map<String, String> params = generateParams("신분당선", "bg-red-600", "1", "2"," 10");
        ExtractableResponse<Response> response = LineUtils.createLine(params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        params = generateParams("분당선", "bg_green-600", "1", "3", "20");
        response = LineUtils.createLine(params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<String> lineNames = LineUtils.getLineNames();
        assertThat(lineNames.size()).isEqualTo(2);
        assertThat(lineNames).containsExactly("신분당선", "분당선");
    }


    /** Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철노선 조회")
    @Test
    void getLine(){
        generateStations(new String[]{"왕십리역", "마장역"});

        //given
        Map<String, String> params = generateParams("신분당선", "bg-red-600", "1", "2"," 10");
        ExtractableResponse<Response> response = LineUtils.createLine(params);
        Long id = response.body().jsonPath().getLong("id");

        //when
        String lineName = LineUtils.getLineName(id);

        //then
        assertThat(lineName).isEqualTo("신분당선");
    }


    /** Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine(){
        generateStations(new String[]{"왕십리역", "마장역"});

        //given
        Map<String, String> params = generateParams("신분당선", "bg-red-600", "1", "2"," 10");
        ExtractableResponse<Response> response = LineUtils.createLine(params);
        Long id = response.jsonPath().getLong("id");

        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "다른분당선");
        updateParams.put("color", "bg-blue-600");

        //when
        response = LineUtils.updateLine(updateParams, id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        List<String> lineNames = LineUtils.getLineNames();
        assertThat(lineNames.size()).isEqualTo(1);
        assertThat(lineNames).containsExactly("다른분당선");
    }

    /** Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */

    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine(){
        generateStations(new String[]{"왕십리역", "마장역"});

        //given
        Map<String, String> params = generateParams("신분당선", "bg-red-600", "1", "2"," 10");
        ExtractableResponse<Response> response = LineUtils.createLine(params);
        Long id = response.jsonPath().getLong("id");

        //when
        response = LineUtils.deleteLine(id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> lineNames = LineUtils.getLineNames();
        assertThat(lineNames.size()).isEqualTo(0);
        assertThat(lineNames).doesNotContain("신분당선");
    }

    private Map<String, String> generateParams(String lineName, String color, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private void generateStations(String[] stationName) {
        List<String> stationNames = new ArrayList<>(Arrays.asList(stationName));
        stationNames.stream().forEach(StationUtils::createStation);
    }
}
