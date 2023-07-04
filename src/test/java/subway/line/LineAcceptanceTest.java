package subway.line;

import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationHelper.지하철역_생성;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private Long firstStationId;
    private Long secondStationId;
    private Long thirdStationId;
    private Long fourthStationId;

    @BeforeEach
    void setUp() {
        테스트용_포트_설정();
        firstStationId = 지하철역_생성_ID("노원역");
        secondStationId = 지하철역_생성_ID("창동역");
        thirdStationId = 지하철역_생성_ID("강남역");
        fourthStationId = 지하철역_생성_ID("사당역");
    }

    /**
     * Given 지하철역들이 등록되어 있다.
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        //given
        final String name = "4호선";
        //when
        지하철_노선_생성(makeRequest(name, "light-blue", firstStationId, secondStationId));
        //then
        List<LineResponse> list = 지하철_노선_목록_조회();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getStations().size()).isEqualTo(2);
        assertThat(list.get(0).getName()).isEqualTo(name);
    }

    /**
     * Given 지하철역들이 등록되어 있다.
     * And 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {
        //given
        지하철_노선_생성(makeRequest("4호선", "light-blue", firstStationId, secondStationId));
        지하철_노선_생성(makeRequest("2호선", "green", thirdStationId, fourthStationId));
        //when
        List<LineResponse> list = 지하철_노선_목록_조회();
        //then
        assertThat(list.size()).isEqualTo(2);
    }

    /**
     * Given 지하철역들이 등록되어 있다.
     * And 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        final String name = "4호선";
        final String color = "light-blue";

        long id = 지하철_노선_생성_ID(makeRequest(name, color, firstStationId, secondStationId));
        //when
        LineResponse line = 지하철_노선_조회(id);
        //then
        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getStations().size()).isEqualTo(2);
        assertThat(line.getColor()).isEqualTo(color);

    }


    /**
     * Given 지하철역들이 등록되어 있다.
     * And 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        final String beforeName = "4호선";
        final String beforeColor = "light-blue";

        final String afterName = "9호선";
        final String afterColor = "brown";

        LineRequest saveRequest = makeRequest(beforeName, beforeColor, firstStationId, secondStationId);
        LineRequest updateRequest = makeRequest(afterName, afterColor, secondStationId, fourthStationId);
        long id = 지하철_노선_생성_ID(saveRequest);
        //when
        지하철_노선_수정(id, updateRequest);
        //then : 지하철 노선 이름, 색깔이 수정됨을 확인
        LineResponse response = 지하철_노선_조회(id);
        assertThat(response.getName()).isEqualTo(afterName);
        assertThat(response.getColor()).isEqualTo(afterColor);

        //then : 지하철 노선에 연결된 역이 수정됨을 확인
        List<Long> stationIds = response.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactly(secondStationId, fourthStationId);
    }

    /**
     * Given 지하철역들이 등록되어 있다.
     * And 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        long id = 지하철_노선_생성_ID(makeRequest("4호선", "light-blue", secondStationId, thirdStationId));
        //when
        지하철_노선_삭제(id);
        //then
        List<LineResponse> response = 지하철_노선_목록_조회();
        assertThat(response.size()).isEqualTo(0);
    }

    private static void 지하철_노선_삭제(Long id) {
        RestAssured.given()
                .when()
                .delete("/lines/" + id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static void 지하철_노선_수정(Long id, LineRequest request) {
        RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    private static List<LineResponse> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", LineResponse.class);
    }

    private long 지하철_노선_생성_ID(LineRequest request) {
        return 지하철_노선_생성(request).getId();
    }

    private static LineResponse 지하철_노선_생성(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getObject(".", LineResponse.class);
    }


    private static LineResponse 지하철_노선_조회(Long id) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", LineResponse.class);
    }

    private long 지하철역_생성_ID(String name) {
        return 지하철역_생성(name)
                .jsonPath()
                .getLong("id");
    }

    private void 테스트용_포트_설정() {
        RestAssured.port = port;
    }

    private LineRequest makeRequest(String name, String color, Long upStationId, Long downStationId) {
        return LineRequest.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(RandomUtils.nextInt())
                .build();
    }
}
