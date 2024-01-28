package subway;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.station.Station;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@Sql(scripts = "/reset-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    public void 지하철_노선_생성() {
        // when
        final LineRequest request = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        final ExtractableResponse<Response> response = createSubwayLine(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final JsonPath jsonPath = this.getSubwayLineList();

        final List<String> lineNames = jsonPath.getList("name", String.class);
        assertThat(lineNames).hasSize(1);
        assertThat(lineNames).containsAnyOf("신분당선");

        final List<Station> lineStations = jsonPath.getList("[0].stations", Station.class);
        assertThat(lineStations).hasSize(2);

        final List<String> lineStationNames = jsonPath.getList("[0].stations.name", String.class);
        assertThat(lineStationNames).containsExactlyInAnyOrder("강남역", "역삼역");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    public void 지하철_노선_목록_조회() {
        // given
        final LineRequest request1 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        createSubwayLine(request1);

        final LineRequest request2 = new LineRequest("지하철노선1", "bg-green-600", 1L, 3L, 15);
        createSubwayLine(request2);

        // when
        final JsonPath jsonPath = this.getSubwayLineList();

        // then
        final List<String> lineNames = jsonPath.getList("name", String.class);
        assertThat(lineNames).hasSize(2);
        assertThat(lineNames).containsExactly("신분당선", "지하철노선1");

        final List<String> lineStationNames = jsonPath.getList("[1].stations.name", String.class);
        assertThat(lineStationNames).doesNotContain("역삼역");
        assertThat(lineStationNames).containsExactly("강남역", "지하철역1");
    }

    private LineRequest createLineRequest(
            final String lineName
            , final String lineColor
            , final Long upStationId
            , final Long downStationId
            , final Integer lineDistance
    ) {
        return new LineRequest(lineName, lineColor, upStationId, downStationId, lineDistance);
    }

    private ExtractableResponse<Response> createSubwayLine(LineRequest request) {
        final ExtractableResponse<Response> response =
                given()
                    .log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .log().all()
                .extract();

        return response;
    }

    private JsonPath getSubwayLineList() {
        final JsonPath response =
                given()
                    .log().all()
                .when()
                    .get("/lines")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .log().all()
                .extract()
                    .jsonPath();

        return response;
    }

}
