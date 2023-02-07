package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LinePatchResponse;
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
        StationAcceptanceTest.createStation("서현역");
    }

    /*
    * 지하철 노선 생성
    * when 지하철 노선을 생성하면
    * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
    * */

    @DisplayName("지하철노선 생성 인수 테스트")
    @Test
    void createLine() {
        //when
        createLine("신분당선", "bg-red-600", 1, 2, 10);

        List<String> lineList = getLines("/lines");

        assertThat(lineList).containsAnyOf("신분당선");
    }


    public void createLine(String name, String color, long upStationId, long downStationId, int distance) {
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


    /*
    * 지하철 노선 목록 조회
    * given 2개의 지하철 노선을 생성하고
    * when 지하철 노선 목록을 조회하면
    * then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    * */

    @DisplayName("지하철노선 목록 조회 인수테스트")
    @Test
    void getLines() {
        //given
        createLine("신분당선", "bg-red-600", 1, 2, 10);
        createLine("분당선", "bg-green-600", 1, 3, 10);

        //when
        List<String> lineList = getLines("/lines");

        //then
        assertThat(lineList).containsExactly("신분당선", "분당선");
    }

    /*
    * given 지하철 노선을 생성하고
    * when 생성한 지하철 노선을 조회하면
    * then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    * */

    @DisplayName("지하철노선 조회 인수테스트")
    @Test
    void getLine() {
        //given
        createLine("신분당선", "bg-red-600", 1, 2, 10);
        //when
        List<String> line = getLine("/lines/1").getList("stations.name", String.class);
        //then
        assertThat(line).containsAnyOf("강남역");
    }

    public JsonPath getLine(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all().extract().jsonPath();
    }

    /*
    * 지하철노선 수정
    * given 지하철 노선을 생성하고
    * when 생성한 지하철 노선을 수정하면
    * then 해당 지하철 노선 정보는 수정된다.
    * */

    @DisplayName("지하철노선 수정 테스트")
    @Test
    void patchLine() {
        //given
        createLine("신분당선", "bg-red-600", 1, 2, 10);

        //when
        ExtractableResponse<Response> response = patchLine("다른분당선", "bg-red-600", "/lines/1");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        String lineName = getLine("/lines/1").getString("name");
        String color = getLine("/lines/1").getString("color");

        assertThat(lineName).isEqualTo("다른분당선");
        assertThat(color).isEqualTo("bg-red-600");

    }

    public ExtractableResponse<Response> patchLine(String name, String color, String path) {
        LinePatchResponse patchResponse = new LinePatchResponse(name, color);

       return RestAssured.given().header("Content-type", "application/json")
               .log().all()
               .and()
               .body(patchResponse)
               .when().patch(path)
               .then().log().all().extract();
    }









}
