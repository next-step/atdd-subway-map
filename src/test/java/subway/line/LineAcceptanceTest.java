package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.section.SectionRepository;
import subway.station.StationRepository;
import subway.station.StationRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        sectionRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-999");
        params.put("upStationId", 강남역_ID);
        params.put("downStationId", 건대입구역_ID);
        params.put("distance", "10");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-green-999");
        assertThat(response.jsonPath().getList("stations")).hasSize(2);
        assertThat(response.jsonPath().getList("stations.id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("강남역", "건대입구역");
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
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        ExtractableResponse<Response> 군자역 = createStation("군자역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
        String 군자역_ID = 군자역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        ExtractableResponse<Response> 칠호선 = createLine("7호선", "bg-orange-600", 건대입구역_ID, 군자역_ID, "20");
        String 이호선_ID = 이호선.jsonPath().getString("id");
        String 칠호선_ID = 칠호선.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".")).hasSize(2);
        assertThat(response.jsonPath().getList("id", String.class)).containsExactly(이호선_ID, 칠호선_ID);
        assertThat(response.jsonPath().getList("name", String.class)).containsExactly("2호선", "7호선");
        assertThat(response.jsonPath().getList("color", String.class)).containsExactly("bg-green-999", "bg-orange-600");
        assertThat(response.jsonPath().getList("stations", String.class)).hasSize(2);
        assertThat(response.jsonPath().getList("stations[0].id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(response.jsonPath().getList("stations[0].name", String.class)).containsExactly("강남역", "건대입구역");
        assertThat(response.jsonPath().getList("stations[1].id", String.class)).containsExactly(건대입구역_ID, 군자역_ID);
        assertThat(response.jsonPath().getList("stations[1].name", String.class)).containsExactly("건대입구역", "군자역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        String 이호선_ID = 이호선.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines/{lineId}", 이호선_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("id")).isEqualTo(이호선_ID);
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-green-999");
        assertThat(response.jsonPath().getList("stations")).hasSize(2);
        assertThat(response.jsonPath().getList("stations.id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("강남역", "건대입구역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        String 이호선_ID = 이호선.jsonPath().getString("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "3호선");
        params.put("color", "bg-blue-222");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().put("/lines/{lineId}", 이호선_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> loadLine = loadLine(Long.valueOf(이호선_ID));
        assertThat(loadLine.jsonPath().getString("id")).isEqualTo(이호선_ID);
        assertThat(loadLine.jsonPath().getString("name")).isEqualTo("3호선");
        assertThat(loadLine.jsonPath().getString("color")).isEqualTo("bg-blue-222");
        assertThat(loadLine.jsonPath().getList("stations")).hasSize(2);
        assertThat(loadLine.jsonPath().getList("stations.id", String.class)).containsExactly(강남역_ID, 건대입구역_ID);
        assertThat(loadLine.jsonPath().getList("stations.name", String.class)).containsExactly("강남역", "건대입구역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> 강남역 = createStation("강남역");
        ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
        String 강남역_ID = 강남역.jsonPath().getString("id");
        String 건대입구역_ID = 건대입구역.jsonPath().getString("id");

        ExtractableResponse<Response> 이호선 = createLine("2호선", "bg-green-999", 강남역_ID, 건대입구역_ID, "10");
        String 이호선_ID = 이호선.jsonPath().getString("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().delete("/lines/{lineId}", 이호선_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> loadLine = loadLine(Long.valueOf(이호선_ID));
        assertThat(loadLine.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 구간 등록")
    @Nested
    class AddSection {

        /**
         * Given 2개의 지하철 역(A, B)이 등록되어 있다.
         * And 1개의 지하철 노선이 등록되어 있다.
         * When 지하철 노선 하행 종착지(B)에 추가로 지하철 구간(B-C)을 등록한다.
         * Then 새로운 지하철 구간이 등록된다. (A-B-C)
         */
        @DisplayName("지하철 노선에 신규 구간을 등록한다.")
        @Test
        void success() {
            // given
            ExtractableResponse<Response> 성수역 = createStation("성수역");
            String 성수역_ID = 성수역.jsonPath().getString("id");
            ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
            String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
            ExtractableResponse<Response> 구의역 = createStation("구의역");
            String 구의역_ID = 구의역.jsonPath().getString("id");

            ExtractableResponse<Response> 이호선 = createLine(
                    "2호선",
                    "bg-green-000",
                    성수역_ID,
                    건대입구역_ID,
                    "10"
            );
            String 이호선_ID = 이호선.jsonPath().getString("id");

            // when
            Map<String, String> params = new HashMap<>();
            params.put("upStationId", 건대입구역_ID);
            params.put("downStationId", 구의역_ID);
            params.put("distance", "10");

            ExtractableResponse<Response> response =
                    RestAssured.given().log().all()
                            .body(params)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/lines/{lineId}/sections", 이호선_ID)
                            .then().log().all()
                            .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.jsonPath().getList("stations")).hasSize(3);
            assertThat(response.jsonPath().getList("stations.id", String.class)).containsExactly(성수역_ID, 건대입구역_ID, 구의역_ID);
            assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("성수역", "건대입구역", "구의역");
        }

        /**
         * Given 2개의 지하철 역(A, C)이 등록되어 있다.
         * And 1개의 지하철 노선이 등록되어 있다.
         * When 지하철 노선 하행 종착지(C)에 추가로 지하철 구간(B-C)을 등록을 시도하면
         * Then 추가 구간(B-C)의 하행역(C)이 이미 노선에 등록되어있어 에러가 발생한다.
         */
        @DisplayName("지하철 노선 구간에 이미 등록되어 있는 역을 추가하려 하면 에러가 발생한다.")
        @Test
        void duplicateStationError() {
            // given
            ExtractableResponse<Response> 성수역 = createStation("성수역");
            String 성수역_ID = 성수역.jsonPath().getString("id");
            ExtractableResponse<Response> 구의역 = createStation("구의역");
            String 구의역_ID = 구의역.jsonPath().getString("id");

            ExtractableResponse<Response> 이호선 = createLine(
                    "2호선",
                    "bg-green-000",
                    성수역_ID,
                    구의역_ID,
                    "10"
            );
            String 이호선_ID = 이호선.jsonPath().getString("id");

            // when
            Map<String, String> params = new HashMap<>();
            params.put("upStationId", 구의역_ID);
            params.put("downStationId", 성수역_ID);
            params.put("distance", "10");

            ExtractableResponse<Response> response =
                    RestAssured.given().log().all()
                            .body(params)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/lines/{lineId}/sections", 이호선_ID)
                            .then().log().all()
                            .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("주어진 하행역은 이미 노선에 등록되어 있는 등록된 역입니다. downStationId: " + 성수역_ID);
        }

        /**
         * Given 2개의 지하철 역(A, B)이 등록되어 있다.
         * And 1개의 지하철 노선이 등록되어 있다.
         * When 지하철 노선 하행 종착지(B)에 추가로 지하철 구간(C-D)을 등록을 시도하면
         * Then 추가 구간(C-D)의 상행역(C)가 노선의 하행 종착역(B)과 다르기 때문에 에러가 발생한다.
         */
        @DisplayName("새로 추가하려는 구간의 상행역이 노선의 하행 종착역과 다른 역이라면 에러가 발생한다.")
        @Test
        void invalidUpStationError() {
            // given
            ExtractableResponse<Response> 성수역 = createStation("성수역");
            String 성수역_ID = 성수역.jsonPath().getString("id");
            ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
            String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
            ExtractableResponse<Response> 구의역 = createStation("구의역");
            String 구의역_ID = 구의역.jsonPath().getString("id");
            ExtractableResponse<Response> 잠실역 = createStation("잠실역");
            String 잠실역_ID = 잠실역.jsonPath().getString("id");

            ExtractableResponse<Response> 이호선 = createLine(
                    "2호선",
                    "bg-green-000",
                    성수역_ID,
                    건대입구역_ID,
                    "10"
            );
            String 이호선_ID = 이호선.jsonPath().getString("id");

            // when
            Map<String, String> params = new HashMap<>();
            params.put("upStationId", 구의역_ID);
            params.put("downStationId", 잠실역_ID);
            params.put("distance", "10");

            ExtractableResponse<Response> response =
                    RestAssured.given().log().all()
                            .body(params)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/lines/{lineId}/sections", 이호선_ID)
                            .then().log().all()
                            .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("새로운 구간의 상행역은 노선의 하행 종착역과 같아야 합니다. upStationId: " + 구의역_ID);
        }
    }

    @DisplayName("지하철 구간 제거")
    @Nested
    class RemoveSection {

        /**
         * Given 3개의 지하철 역(A, B, C)이 등록되어 있다.
         * And 1개의 지하철 노선이 등록되어 있다.
         * And 지하철 노선에 2개의 구간(A-B, B-C)이 등록되어 있다.
         * When 지하철 노선에서 하행 종착역(C)을 이용해서 구간(B-C)을 제거한다.
         * Then 지하철 노선에 남은 구간 목록은 1개(A-B)이다.
         */
        @DisplayName("지하철 노선에서 구간을 제거한다.")
        @Test
        void success() {
            // given
            ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
            String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
            ExtractableResponse<Response> 구의역 = createStation("구의역");
            String 구의역_ID = 구의역.jsonPath().getString("id");
            ExtractableResponse<Response> 강남역 = createStation("강남역");
            String 강남역_ID = 강남역.jsonPath().getString("id");

            ExtractableResponse<Response> 이호선 = createLine(
                    "2호선",
                    "bg-green-000",
                    건대입구역_ID,
                    구의역_ID,
                    "10"
            );
            String 이호선_ID = 이호선.jsonPath().getString("id");

            ExtractableResponse<Response> 건대입구역_구의역_구간 = addSection(이호선_ID, 구의역_ID, 강남역_ID, "10");

            // when
            ExtractableResponse<Response> response =
                    RestAssured.given().log().all()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().delete("/lines/{lineId}/sections?stationId={stationId}", 이호선_ID, 강남역_ID)
                            .then().log().all()
                            .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            ExtractableResponse<Response> loadLine = loadLine(Long.valueOf(이호선_ID));
            assertThat(loadLine.jsonPath().getList("stations")).hasSize(2);
            assertThat(loadLine.jsonPath().getList("stations.id", String.class)).containsExactly(건대입구역_ID, 구의역_ID);
            assertThat(loadLine.jsonPath().getList("stations.name", String.class)).containsExactly("건대입구역", "구의역");
        }

        /**
         * Given 3개의 지하철 역(A, B, C)이 등록되어 있다.
         * And 1개의 지하철 노선이 등록되어 있다.
         * And 지하철 노선에 2개의 구간(A-B, B-C)이 등록되어 있다.
         * When 지하철 노선에서 하행 종착역이 아닌 역(B)을 이용해서 구간을 제거한다.
         * Then 지하철 노선의 가장 마지막 구간이 아니므로 에러가 발생한다.
         */
        @DisplayName("지하철 노선에서 하행 종착역이 아닌 역을 이용해서 구간을 제거하면 에러가 발생한다.")
        @Test
        void invalidLastStationError() {
            // given
            ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
            String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
            ExtractableResponse<Response> 구의역 = createStation("구의역");
            String 구의역_ID = 구의역.jsonPath().getString("id");
            ExtractableResponse<Response> 강남역 = createStation("강남역");
            String 강남역_ID = 강남역.jsonPath().getString("id");

            ExtractableResponse<Response> 이호선 = createLine(
                    "2호선",
                    "bg-green-000",
                    건대입구역_ID,
                    구의역_ID,
                    "10"
            );
            String 이호선_ID = 이호선.jsonPath().getString("id");

            ExtractableResponse<Response> 건대입구역_구의역_구간 = addSection(이호선_ID, 구의역_ID, 강남역_ID, "10");

            // when
            ExtractableResponse<Response> response =
                    RestAssured.given().log().all()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().delete("/lines/{lineId}/sections?stationId={stationId}", 이호선_ID, 구의역_ID)
                            .then().log().all()
                            .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("노선의 하행 종착역만 삭제할 수 있습니다. stationId: " + 구의역_ID);
        }

        /**
         * Given 3개의 지하철 역(A, B, C)이 등록되어 있다.
         * And 1개의 지하철 노선이 등록되어 있다.
         * And 지하철 노선에 1개의 구간(A-B)이 등록되어 있다.
         * When 지하철 노선에서 하행 종착역(B)을 이용해서 구간을 제거한다.
         * Then 지하철 노선에 구간이 1개뿐인 경우 구간을 제거할 수 없어 에러가 발생한다.
         */
        @DisplayName("지하철 노선에서 구간이 1개뿐인 경우 구간을 제거할 수 없어 에러가 발생한다.")
        @Test
        void invalidSectionSizeError() {
            // given
            ExtractableResponse<Response> 건대입구역 = createStation("건대입구역");
            String 건대입구역_ID = 건대입구역.jsonPath().getString("id");
            ExtractableResponse<Response> 구의역 = createStation("구의역");
            String 구의역_ID = 구의역.jsonPath().getString("id");

            ExtractableResponse<Response> 이호선 = createLine(
                    "2호선",
                    "bg-green-000",
                    건대입구역_ID,
                    구의역_ID,
                    "10"
            );
            String 이호선_ID = 이호선.jsonPath().getString("id");

            // when
            ExtractableResponse<Response> response =
                    RestAssured.given().log().all()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().delete("/lines/{lineId}/sections?stationId={stationId}", 이호선_ID, 구의역_ID)
                            .then().log().all()
                            .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("노선에 남은 구간이 1개뿐이라 삭제할 수 없습니다.");
        }
    }


    private ExtractableResponse<Response> createStation(String stationName) {
        StationRequest request = new StationRequest(stationName);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createLine(String lineName, String lineColor, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> loadLine(Long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> addSection(String lineId, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
