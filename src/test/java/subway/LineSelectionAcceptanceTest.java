package subway;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineAppendRequest;
import subway.line.LineAppendResponse;
import subway.line.LineRequest;
import subway.line.LineResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static subway.LineAcceptanceTest.parseCreateLineResponse;
import static subway.LineAcceptanceTest.requestCreateLine;


@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
@Sql(value = {"/LineSelectionAcceptance.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineSelectionAcceptanceTest {

    @DisplayName("상행-하행만 있는 노선에 구간을 등록하는 기능")
    @Test
    void appendLineSelection1() {
        //given 지하철 노선을 생성하면.
        LineRequest createLineReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        LineResponse createLineRes = parseCreateLineResponse(requestCreateLine(createLineReq));


        //when 상행-하행 2가지 역만 있는 노선에 구간을 등록하면
        LineAppendRequest lineAppendRequest = new LineAppendRequest("2", "4", 10L);
        LineAppendResponse lineAppendResponse = appendSelection(createLineRes.getId(), lineAppendRequest.getUpStationId(), lineAppendRequest.getDownStationId(), lineAppendRequest.getDistance()).then().extract().jsonPath().getObject("$", LineAppendResponse.class);;


        //then 노선에 있는 역은 3개가 되고, 하행은 추가된 노선의 것을 따른다.
        LineAppendResponse checkLineAppendResponse = new LineAppendResponse(createLineRes.getId(), createLineRes.getName(), createLineReq.getUpStationId(), lineAppendResponse.getDownStationId(), createLineReq.getDistance() + lineAppendRequest.getDistance(), List.of(1L, 2L, 4L));

        checkLineAppendResponse(lineAppendResponse, checkLineAppendResponse);
    }

    @DisplayName("3개 이상 역이 있는 노선에 구간을 등록하는 기능")
    @Test
    void appendLineSelection2() {
        List<Long> stations = List.of(1L, 2L, 3L, 4L);
        List<Long> distances = List.of(10L, 3L, 5L);
        Long sumDistance = distances.stream().reduce(0L, Long::sum);

        //given 3개 이상 역이 있는 노선에
        LineAppendResponse createdLine = createLineWithStations(stations, distances, "신분당선", "bg-red-600");

        //when 새로운 구간을 추가하면
        LineAppendRequest lineAppendRequest = new LineAppendRequest("4", "5", 10L);
        LineAppendResponse lineAppendResponse = appendSelection(createdLine.getId(), lineAppendRequest.getUpStationId(), lineAppendRequest.getDownStationId(), lineAppendRequest.getDistance()).then().extract().jsonPath().getObject("$", LineAppendResponse.class);;


        //then station 하나 추가되고, 하행은 새로운 것을 따른다.
        List<Long> checkStation = new ArrayList<>(stations);
        checkStation.add(Long.parseLong(lineAppendRequest.getDownStationId()));
        LineAppendResponse checkLineAppendResponse = new LineAppendResponse(createdLine.getId(), createdLine.getLineName(), createdLine.getUpStationId(), Long.parseLong(lineAppendRequest.getDownStationId()), sumDistance + lineAppendRequest.getDistance(),
                checkStation);

        checkLineAppendResponse(lineAppendResponse, checkLineAppendResponse);
    }


    @DisplayName("추가되는 구간의 상행역은 기존의 하행 종점역 이여야 한다.")
    @Test
    void appendLineSelection3() {
        List<Long> stations = List.of(1L, 2L, 3L, 4L);
        List<Long> distances = List.of(10L, 3L, 5L);
        Long sumDistance = distances.stream().reduce(0L, Long::sum);

        //given 3개 이상 역이 있는 노선에
        LineAppendResponse createdLine = createLineWithStations(stations, distances, "신분당선", "bg-red-600");

        //when 조건에 만족하지 않는 새로운 노선을 추가하면
        LineAppendRequest lineAppendRequest = new LineAppendRequest("5", "6", 10L);

        //then 에러를 리턴한다.
        Response response = appendSelection(createdLine.getId(), lineAppendRequest.getUpStationId(), lineAppendRequest.getDownStationId(), lineAppendRequest.getDistance());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @DisplayName("추가되는 구간의 하행역은 기존의 노선의 역이 아니여야 된다.")
    @Test
    void appendLineSelection4() {
        List<Long> stations = List.of(1L, 2L, 3L, 4L);
        List<Long> distances = List.of(10L, 3L, 5L);
        Long sumDistance = distances.stream().reduce(0L, Long::sum);

        //given 3개 이상 역이 있는 노선에
        LineAppendResponse createdLine = createLineWithStations(stations, distances, "신분당선", "bg-red-600");

        //when 조건에 만족하지 않는 새로운 노선을 추가하면
        LineAppendRequest lineAppendRequest = new LineAppendRequest("4", "3", 10L);

        //then 에러를 리턴한다.
        Response response = appendSelection(createdLine.getId(), lineAppendRequest.getUpStationId(), lineAppendRequest.getDownStationId(), lineAppendRequest.getDistance());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void checkLineAppendResponse(LineAppendResponse actual, LineAppendResponse matcher) {
        assertThat(actual.getLineName()).isEqualTo(matcher.getLineName());
        assertThat(actual.getUpStationId()).isEqualTo(matcher.getUpStationId());
        assertThat(actual.getDownStationId()).isEqualTo(matcher.getDownStationId());
        assertThat(actual.getStations()).isEqualTo(matcher.getStations());
        assertThat(actual.getDistance()).isEqualTo(matcher.getDistance());
    }

    /**
     * N개의 역이 있는 노선을 등록하는 메소드
     */
    private LineAppendResponse createLineWithStations(List<Long> stations, List<Long> distances, String lineName, String lineColor) {
        assertThat(stations.size()).isGreaterThan(1);
        assertThat(stations.size()).isEqualTo(distances.size() + 1);

        LineRequest createLineReq = new LineRequest(lineName, lineColor, stations.get(0), stations.get(1), distances.get(0));
        LineResponse createLineRes = parseCreateLineResponse(requestCreateLine(createLineReq));

        Response response = null;
        for (int i = 1; i < stations.size() - 1; i++) {
            response = appendSelection(createLineRes.getId(), Long.toString(stations.get(i)), Long.toString(stations.get(i + 1)), distances.get(i));
        }
        return response.then().extract().jsonPath().getObject("$", LineAppendResponse.class);
    }

    
    /**
     * 기존 노선에 구간을 등록하는 메소드
     */
    private Response appendSelection(Long lineId, String upStationId, String downStationId, Long distance) {
        LineAppendRequest lineAppendRequest = new LineAppendRequest(upStationId, downStationId, distance);
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineAppendRequest)
                .when().post("/lines/" + lineId + "/selections");
        return response;
    }
}
