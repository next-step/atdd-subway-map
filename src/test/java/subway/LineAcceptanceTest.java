package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineRepository;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;
import subway.subway.Station;
import subway.subway.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private LineRepository lineRepository;

    StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();

    Long firstStationId;
    Long secondStationId;
    Long thirdStationId;

    LineRequest lineRequest;

    @BeforeEach
    public void setup() {
        firstStationId = stationAcceptanceTest.createStation("지하철역1");
        secondStationId = stationAcceptanceTest.createStation("지하철역2");
        thirdStationId = stationAcceptanceTest.createStation("지하철역3");
        lineRequest = new LineRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10L);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 등록하고 생성한 노선을 찾을 수 있다..")
    @Test
    public void createLineTest() {

        // When
        createLine(lineRequest);

        // Then
        var lineResponseList = getLineResponseList();
        List<String> nameList = lineResponseList.stream().map(LineResponse::getName).collect(Collectors.toList());

        assertThat(lineResponseList).hasSize(1);
        assertThat(nameList).containsExactly(lineRequest.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성하고 목록을 조회하면 지하철 목록 조회시 2개의 노선을 조회할 수 있다.")
    @Test
    void getLineList() {

        // When
        LineRequest secondLineRequest = new LineRequest("분당선", "bg-red-600", firstStationId, thirdStationId, 10L);
        createLine(lineRequest);
        createLine(secondLineRequest);

        // Then
        var lineResponseList = getLineResponseList();
        List<String> nameList = lineResponseList.stream().map(LineResponse::getName).collect(Collectors.toList());

        assertThat(lineResponseList).hasSize(2);
        assertThat(nameList).containsAnyOf(lineRequest.getName());
        assertThat(nameList).containsAnyOf(secondLineRequest.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 해당 지하철 노선을 조회하면 지하철 노선 정보를 조회할 수 있다.")
    @Test
    public void getLineTest() {
        Long lineId = createLine(lineRequest);
        var line = getLine(lineId);

        checkLine(lineRequest, lineId, line, firstStationId, secondStationId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 생성하고 해당 지하철 노선을 수정하면 수정된다.")
    @Test
    public void updateLineTest() {
        Long id = createLine(lineRequest);
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("다른 분당선", "bg-red-600");
        updateLine(id, lineUpdateRequest);
        LineResponse line = getLine(id);

        assertThat(line.getId()).isEqualTo(id);
        assertThat(line.getName()).isEqualTo(lineUpdateRequest.getName());
    }

    @DisplayName("지하철 노선도를 수정할 수 있다.")
    private void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        var response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineUpdateRequest)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */

    @DisplayName("지하철 노선도를 조회할 수 있다.")
    private List<LineResponse> getLineResponseList() {
        var response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.jsonPath().getList(".", LineResponse.class);
    }

    @DisplayName("지하철 노선을 등록할 수 있다.")
    private Long createLine(LineRequest lineRequest) {
        var response = RestAssured.given().log().all()
                .body(lineRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.jsonPath().getLong("id");
    }

    @DisplayName("입력한 지하철 노선도 정보와 조회한 지하철 노선도 정보를 비교할 수 있다.")
    private void checkLine(LineRequest lineRequest, Long lineId, LineResponse lineResponse, Long stationId1, Long stationId2) {
        var stationIdList = lineResponse.getStationResponseList().stream().mapToLong(StationResponse::getId);

        assertAll(
                () -> assertThat(lineResponse.getId()).isEqualTo(lineId),
                () -> assertThat(lineResponse.getName()).isEqualTo(lineRequest.getName()),
                () -> assertThat(stationIdList).containsAnyOf(stationId1, stationId2)
        );
    }

    @DisplayName("지하철 노선 아이디를 통해 해당 지하철 노선을 조회할 수 있다.")
    private LineResponse getLine(Long lineId) {
        var response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.as(LineResponse.class);
    }


}
