package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.Map;

import static nextstep.subway.acceptance.TestSetupUtils.buildLine;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하촐 노선 관리기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    DbCleanup dbCleanup;

    @Autowired
    StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        dbCleanup.execute();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        long lineId = buildLine("신분당선", "red", "강남역", "교대역")
                .jsonPath()
                .getLong("id");

        // then
        assertThat(linesResponse().jsonPath().getList("id", Long.class)).contains(lineId);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        long firstId = buildLine("신분당선", "red", "강남역", "교대역")
                .jsonPath()
                .getLong("id");

        long secondId = buildLine("2호선", "green", "역삼역", "선릉역")
                .jsonPath()
                .getLong("id");

        // when
        ExtractableResponse<Response> linesResponse = linesResponse();

        // then
        assertThat(linesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(linesResponse.jsonPath().getList("id", Long.class)).containsExactly(firstId, secondId);
    }

    private ExtractableResponse<Response> linesResponse() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하촐 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        long lineId = buildLine("신분당선", "red", "강남역", "교대역")
                .jsonPath()
                .getLong("id");

        // when
        ExtractableResponse<Response> lineResponse = lineResponse(lineId);

        // then
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.jsonPath().getLong("id")).isEqualTo(lineId);
    }

    /**
     * Given 지하철 노선을 생성고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하촐 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        long lineId = buildLine("신분당선", "red", "강남역", "교대역")
                .jsonPath()
                .getLong("id");

        // when
        RestAssured.given().log().all()
                .body(Map.of("name", "다른분당선",
                        "color", "orange"))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(URI.create("/lines/" + lineId))
                .then().log().all();

        // then
        ExtractableResponse<Response> updatedLineResponse = lineResponse(lineId);
        assertThat(updatedLineResponse.jsonPath().getString("name")).isEqualTo("다른분당선");
        assertThat(updatedLineResponse.jsonPath().getString("color")).isEqualTo("orange");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하촐 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        long lineId = buildLine("신분당선", "red", "강남역", "교대역")
                .jsonPath()
                .getLong("id");

        // when
       RestAssured.given().log().all()
                .when().delete(URI.create("/lines/" + lineId))
                .then().log().all()
                .extract();

        // then
        ExtractableResponse<Response> deletedLineResponse = lineResponse(lineId);
        assertThat(deletedLineResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> lineResponse(long lineId) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(URI.create("/lines/" + lineId))
                .then().log().all()
                .extract();
    }
}
