package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        지하철_노선_생성(name, "light-blue", firstStationId, secondStationId, 10);
        //then
        List<LineResponse> list = 지하철_노선_목록_조회();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getStations().size()).isEqualTo(2);
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
        지하철_노선_생성("4호선", "light-blue", firstStationId, secondStationId, 1);
        지하철_노선_생성("2호선", "green", thirdStationId, fourthStationId, 5);
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

    /**
     * Given 지하철역들이 등록되어 있다.
     * And 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */

    /**
     * Given 지하철역들이 등록되어 있다.
     * And 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    private static List<LineResponse> 지하철_노선_목록_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", LineResponse.class);
    }

    private static ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }


    private long 지하철역_생성_ID(String name) {
        return 지하철역_생성(name).jsonPath().getLong("id");
    }

    private void 테스트용_포트_설정() {
        RestAssured.port = port;
    }
}
