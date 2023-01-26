package subway.line;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.StationAcceptanceTest;
import subway.StationRepository;
import subway.StationResponse;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.repository.LineRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
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
    void 지하철노선을_생성한다() {
        //given
        String name = "신분당선";
        String color = "bg-red-600";
        long upStationId = StationAcceptanceTest.createStation("상행역");
        long downStationId = StationAcceptanceTest.createStation("하행역");
        long distance = 10;
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);

        // when 지하철 노선을 생성하면
        long id = requestCreateLine(lineRequest).getId();

        // then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
        List<LineResponse> lineList = requestFindAllLines();

        LineResponse created = lineList.get(0);

        assertThat(lineList).hasSize(1);
        assertThat(created.getId()).isEqualTo(id);
        assertThat(created.getName()).isEqualTo(name);
        assertThat(created.getColor()).isEqualTo(color);
        assertThat(created.getStations()).hasSize(2);
        assertThat(created.getStations().get(0).getId()).isEqualTo(upStationId);
        assertThat(created.getStations().get(1).getId()).isEqualTo(downStationId);
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
    void 지하철노선_목록_조회() {
        //given 2개의 지하철노선을 생성하고
        createLine("강남역");
        createLine("역삼역");

        //when 지하철노선 목록을 조회하면
        List<String> stationNames = when().get("/stations")
                .then()
                .extract()
                .jsonPath()
                .getList("name", String.class);

        //then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(stationNames).contains("강남역", "역삼역"); // contains 를 이용해 두 역이 모두 포함되어 있는지 검증
    }

    private List<LineResponse> requestFindAllLines() {
        return when().get("/lines")
                .then()
                .extract()
                .jsonPath().getList(".", LineResponse.class);
    }

    private String createLine(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath()
                .getString("id"); // 응답 객체에서 id값을 추출해 리턴
    }
}