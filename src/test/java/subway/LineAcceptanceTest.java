package subway;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.dtos.request.LineRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 기능 테스트")
@Sql("classpath:sql/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {


    private LineRequest lineRequest;

    @BeforeEach
    void setUp(){
        lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        generateStations("왕십리역", "마장역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */

    @DisplayName("지하철노선 생성")
    @Test
    void createLine(){

        //when
        LineApiTest.createLine(lineRequest);

        //then
        List<String> lineNames = LineApiTest.getLineNames();
        assertThat(lineNames).containsAnyOf("신분당선");
    }


    /** Given 2개의 지하철 노선을 생성하고
    *When 지하철 노선 목록을 조회하면
    *Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    */

    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines(){
        generateStations("동작역");

        //given
        LineApiTest.createLine(lineRequest);

        lineRequest = new LineRequest("분당선", "bg_green-600", 1L, 3L, 20L);
        LineApiTest.createLine(lineRequest);

        //when
        List<String> lineNames = LineApiTest.getLineNames();
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

        //given
        Long id = getLineId(LineApiTest.createLine(lineRequest));

        //when
        String lineName = LineApiTest.getLineName(id);

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

        //given
        Long id = getLineId(LineApiTest.createLine(lineRequest));

        LineRequest updateRequest = new LineRequest("다른분당선", "bg-blue-600");

        //when
        LineApiTest.updateLine(updateRequest, id);

        //then
        List<String> lineNames = LineApiTest.getLineNames();
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

        //given
        Long id = getLineId(LineApiTest.createLine(lineRequest));

        //when
        LineApiTest.deleteLine(id);

        //then
        List<String> lineNames = LineApiTest.getLineNames();
        assertThat(lineNames.size()).isEqualTo(0);
        assertThat(lineNames).doesNotContain("신분당선");
    }

    private void generateStations(String... stationName ) {
        List<String> stationNames = new ArrayList<>(Arrays.asList(stationName));
        stationNames.stream().forEach(StationApiTest::createStation);
    }

    private Long getLineId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

}
