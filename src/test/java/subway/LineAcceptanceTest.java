package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        StationAcceptanceTest.createStation("강남역");
        StationAcceptanceTest.createStation("양재역");
    }


    /*
    * 지하철 노선 생성
    * when 지하철 노선을 생성하면
    * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
    * */

    @DisplayName("지하철 노선 생성 인수 테스트")
    @Test
    void createLine() {
        //when
        createLine("신분당선", "bg-red-600", 1, 2, 10);

        //then
        List<String> lineList = getLines("/lines");
        assertThat(lineList).containsAnyOf("신분당선");
    }


    private void createLine(String name, String color, long upStationId, long downStationId, int distance) {
        LineRequest param = new LineRequest(name, color, upStationId, downStationId, distance);

        RestAssured.given().log().all()
                .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();
    }

    public List<String> getLines(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all().extract().jsonPath().getList("name", String.class);
    }





}
