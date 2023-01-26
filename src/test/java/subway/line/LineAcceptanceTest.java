package subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.repository.LineRepository;
import subway.station.StationAcceptanceTest;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void reset() {
        lineRepository.deleteAll();
        stationRepository.deleteAll();
    }

    @Test
    void 지하철_노선을_생성한다() {
        //given
        LineRequest lineRequest = createLineRequestFixture("신분당선");

        // when 지하철 노선을 생성하면
        long id = requestCreateLine(lineRequest).getId();

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        List<LineResponse> lineList = requestFindAllLines();

        LineResponse created = lineList.get(0);

        assertThat(lineList).hasSize(1);
        assertThat(created.getId()).isEqualTo(id);
        assertThat(created.getName()).isEqualTo(lineRequest.getName());
        assertThat(created.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(created.getStations()).hasSize(2);
        assertThat(created.getStations().get(0).getId()).isEqualTo(lineRequest.getUpStationId());
        assertThat(created.getStations().get(1).getId()).isEqualTo(lineRequest.getDownStationId());
    }

    @Test
    void 지하철_노선_목록_조회() {
        //given 2개의 지하철노선을 생성하고
        long id1 = requestCreateLine(createLineRequestFixture("1호선")).getId();
        long id2 = requestCreateLine(createLineRequestFixture("2호선")).getId();

        //when 지하철노선 목록을 조회하면
        List<LineResponse> findLines = requestFindAllLines();
        List<Long> ids = findLines.stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        //then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(findLines).hasSize(2);
        assertThat(ids).contains(id1, id2);
    }

    private LineRequest createLineRequestFixture(String name) {
        String color = "bg-red-600";
        long upStationId = StationAcceptanceTest.createStation(name + "-상행역");
        long downStationId = StationAcceptanceTest.createStation(name + "-하행역");
        long distance = 10;
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    private LineResponse requestCreateLine(LineRequest lineRequest) {
        return given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(LineResponse.class);
    }

    @Test
    void 지하철_노선_조회() {
        //Given 지하철 노선을 생성하고
        //createLine("강남역");
        //When 생성한 지하철 노선을 조회하면
        //Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    }

    @Test
    void 지하철_노선_수정() {
        //Given 2개의 지하철 노선을 생성하고
        //When 지하철 노선 목록을 조회하면
        //Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    }

    @Test
    void 지하철_노선_삭제() {
        //Given 지하철 노선을 생성하고
        //When 생성한 지하철 노선을 삭제하면
        //Then 해당 지하철 노선 정보는 삭제된다
    }

    private List<LineResponse> requestFindAllLines() {
        return when().get("/lines")
                .then()
                .extract()
                .jsonPath().getList(".", LineResponse.class);
    }
}