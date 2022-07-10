package nextstep.subway.acceptance;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.subway.acceptance.SubwayStationCommon.createdStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.utils.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SubwayLineAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
            databaseCleanUp.afterPropertiesSet();
        }

        databaseCleanUp.execute();

        createdStation("사평역");
        createdStation("신논현");
        createdStation("언주역");
        createdStation("강남역");
        createdStation("양재역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when - 지하철 노선을 생성한다
        ExtractableResponse<Response> response = createdSubwayLine("9호선", "bg-brown-600", 1L, 2L, 10);

        // then - 지하철 목록 조회 시 생성한 노선을 찾을 수 있다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLines() {
        // when - 지하철 노선을 생성한다
        createdSubwayLine("9호선", "bg-brown-600", 1L, 2L, 10);
        createdSubwayLine("신분당선", "bg-red-600", 4L, 5L, 10);

        // when - 지하철 노선을 조회한다
        ExtractableResponse<Response> response = getSubwayLines();

        // then
        List<String> names = response.jsonPath().getList("name", String.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(names).contains("9호선", "신분당선"),
                () -> assertThat(names).hasSize(2)
        );
    }

    private ExtractableResponse<Response> createdSubwayLine(String name, String color, Long upStationId,
                                                            Long downStationId, int distance) {
        return RestAssured.given().log().all()
                .body(createSubwayLineParams(name, color, upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private Map<String, String> createSubwayLineParams(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return params;
    }

    private ExtractableResponse<Response> getSubwayLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }
}
