package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import fixture.LineFixture;
import fixture.StationFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.Station;
import subway.station.StationRepository;
import support.annotation.AcceptanceTest;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
class LineAcceptanceTest {


    public static final String LINE_ONE = "1호선";
    public static final String LINE_TWO = "2호선";
    public static final String COLOR_ONE = "1호선 노선색";
    public static final String COLOR_TWO = "2호선 노선색";
    public static final Long DISTANCE = 100L;
    public static final String STATION_NAME = "지하철역1";
    public static final String STATION_NAME_TWO = "역2";


    /**
     * Given 지하철 역이 2개 존재하고
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다.
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Station upStation = stationRepository.save(StationFixture.giveOne(STATION_NAME));
        Station downStation = stationRepository.save(StationFixture.giveOne(STATION_NAME_TWO));

        // when
        Map<String, Object> request = createLineRequest(
            LINE_TWO,
            COLOR_ONE,
            upStation.getId(),
            downStation.getId(),
            DISTANCE
        );
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            List<String> lineNames = getLineListResponse()
                .jsonPath()
                .getList("name", String.class);
            assertThat(lineNames).containsAnyOf(LINE_TWO);
        });

    }

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLineList() {
        // given
        Station upStation = stationRepository.save(StationFixture.giveOne(STATION_NAME));
        Station downStation = stationRepository.save(StationFixture.giveOne(STATION_NAME_TWO));
        lineRepository.saveAll(
            List.of(
                LineFixture.giveOne(
                    LINE_ONE, COLOR_ONE, upStation, downStation, DISTANCE
                ),
                LineFixture.giveOne(
                    LINE_TWO, COLOR_ONE, upStation, downStation, DISTANCE
                )
            )
        );

        // when
        ExtractableResponse<Response> response = getLineListResponse();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.body().as(List.class)).hasSize(2);
            assertThat(response.jsonPath().getList("name")).containsAnyOf(LINE_ONE, LINE_TWO);
        });

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
        Station upStation = stationRepository.save(StationFixture.giveOne(STATION_NAME));
        Station downStation = stationRepository.save(StationFixture.giveOne(STATION_NAME_TWO));
        Line line = lineRepository.save(
            LineFixture.giveOne(
                LINE_ONE, COLOR_ONE, upStation, downStation, DISTANCE
            )
        );

        // when
        ExtractableResponse<Response> response = getLineResponse(line.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getLong("id")).isEqualTo(line.getId());
        });

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
        Station upStation = stationRepository.save(StationFixture.giveOne(STATION_NAME));
        Station downStation = stationRepository.save(StationFixture.giveOne(STATION_NAME_TWO));
        Line line = lineRepository.save(
            LineFixture.giveOne(
                LINE_ONE, COLOR_ONE, upStation, downStation, DISTANCE
            )
        );

        // when
        Map<String, Object> request = updateLineRequest(
            LINE_TWO,
            COLOR_TWO
        );
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{lineId}", line.getId())
                .then().log().all()
                .extract();

        // then
        ExtractableResponse<Response> lineResponse = getLineResponse(line.getId());
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(lineResponse.jsonPath().getString("name")).isEqualTo(LINE_TWO);
            assertThat(lineResponse.jsonPath().getString("color")).isEqualTo(COLOR_TWO);
        });

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        Station upStation = stationRepository.save(StationFixture.giveOne(STATION_NAME));
        Station downStation = stationRepository.save(StationFixture.giveOne(STATION_NAME_TWO));
        Line line = lineRepository.save(
            LineFixture.giveOne(
                LINE_ONE, COLOR_ONE, upStation, downStation, DISTANCE
            )
        );

        // when
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", line.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(getLineListResponse().jsonPath().getList("id")).doesNotContain(line.getId());
        });

    }

    /**
     * Given 지하철 역이 2개 존재하고
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다.
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineSections() {
        // given
        Station upStation = stationRepository.save(StationFixture.giveOne(STATION_NAME));
        Station downStation = stationRepository.save(StationFixture.giveOne(STATION_NAME_TWO));

        // when
        Map<String, Object> request = createLineRequest(
            LINE_TWO,
            COLOR_ONE,
            upStation.getId(),
            downStation.getId(),
            DISTANCE
        );
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            List<String> lineNames = getLineListResponse()
                .jsonPath()
                .getList("name", String.class);
            assertThat(lineNames).containsAnyOf(LINE_TWO);
        });

    }


    private ExtractableResponse<Response> getLineResponse(Long lineId) {
        return RestAssured.given().log().all()
            .when().get("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();
    }


    private ExtractableResponse<Response> getLineListResponse() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private Map<String, Object> createLineRequest(
        String name,
        String color,
        Long upStationId,
        Long downStationId,
        Long distance
    ) {
        return Map.of(
            "name", name,
            "color", color,
            "upStationId", upStationId,
            "downStationId", downStationId,
            "distance", distance
        );
    }

    private Map<String, Object> updateLineRequest(
        String name,
        String color
    ) {
        return Map.of(
            "name", name,
            "color", color
        );
    }

}
