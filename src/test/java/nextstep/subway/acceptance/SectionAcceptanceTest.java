package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.LineClient;
import nextstep.subway.StationClient;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.station.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관리 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    StationClient stationClient;

    LineClient lineClient;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        stationClient = new StationClient();
        stationClient.create("지하철역", "새로운지하철역", "또다른지하철역", "또또다른지하철역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선 구간 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철 노선에 구간을 생성한다.")
    @Test
    void createSection() {
        // given
        lineClient = new LineClient();
        lineClient.create(params());

        // when
        lineClient.addSection("4", "2", 10, 1);

        // then
        final var root = "";
        final var lineResponses = lineClient.findAll().jsonPath().getList(root, LineResponse.class);
        final var stations = lineResponses.get(0).getStationResponse();
        assertThat(stations.stream().map(StationResponse::getName))
                .containsExactly("지하철역", "새로운지하철역", "또또다른지하철역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 등록할 때, 노선의 하행 종점역과 새로운 구간의 상행역이 일치하지 않으면
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선에 구간을 생성 시 노선의 하행 종점역과 새로운 구간의 상행역이 일치하지 않으면 익셉션 발생한다.")
    @Test
    void createSectionStationMismatchException() {
        // given
        lineClient = new LineClient();
        lineClient.create(params());

        // when
        ExtractableResponse<Response> response = lineClient.addSection("4", "3", 10, 1);
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage"))
                        .isEqualTo("노선의 하행 마지막역과 추가되는 구간의 상행역이 달라 추가될 수 없습니다. 하행 마지막 역 : 2, 구간 상행역 : 3")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 노선에 구간을 등록할 때, 새로운 구간의 하행역이 노선에 등록되어 있다면
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선에 구간을 생성 시 새로운 구간의 하행역이 노선에 등록되어 있다면 익셉션 발생한다.")
    @Test
    void createSectionAlreadyExistStationException() {
        // given
        lineClient = new LineClient();
        lineClient.create(params());

        // when
        ExtractableResponse<Response> response = lineClient.addSection("1", "2", 10, 1);
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage"))
                        .isEqualTo("이미 존재하는 역입니다.")
        );
    }

    /**
     * Given 구간을 등록하고
     * When 구간에 등록된 지하철역을 삭제하면
     * Then 지하철 노선에서 지하철 역은 없어진다
     */
    @DisplayName("지하철 노선 구간에서 지하철역을 삭제한다.")
    @Test
    void deleteStationInSection() {
        // given
        lineClient = new LineClient();
        lineClient.create(params());
        lineClient.addSection("4", "2", 10, 1);

        // when
        lineClient.deleteSection(1L, 4L);
        // then
        final var root = "";
        final var lineResponses = lineClient.findById(1L).jsonPath().getObject(root, LineResponse.class);
        final var stations = lineResponses.getStationResponse();
        assertThat(stations.stream().map(StationResponse::getName)).containsExactly("지하철역", "새로운지하철역");
    }

    /**
     * Given 구간을 등록하고
     * When 구간에 등록된 하행 종점역이 아닌 역을 삭제하면
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선 구간에서 지하철역을 삭제 시 하행 종점역이 아니면 익셉션이 발생한다.")
    @Test
    void deleteStationDownTerminalStationException() {
        // given
        lineClient = new LineClient();
        lineClient.create(params());
        lineClient.addSection("4", "2", 10, 1);

        // when
        ExtractableResponse<Response> response = lineClient.deleteSection(1L, 2L);
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage"))
                        .isEqualTo("하행역만 삭제할 수 있습니다.")
        );
    }


    /**
     * Given 지하철 노선을 등록하고, 하행역을 삭제한 후
     * When 구간에 등록된 상행 종점역과 하행 종점역만 있는 경우 삭제 시
     * Then 에러가 발생한다
     */
    @DisplayName("지하철 노선 구간에서 지하철역을 삭제 시 상행 종점역과 하행 종점역만 있는 경우 익셉션이 발생한다.")
    @Test
    void deleteStationInOnlyOneStationSectionException() {
        // given
        lineClient = new LineClient();
        lineClient.create(params());
        lineClient.deleteSection(1L, 2L);
        // when
        ExtractableResponse<Response> response = lineClient.deleteSection(1L, 1L);
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage"))
                        .isEqualTo("상행역과 하행역만 존재하기 때문에 삭제할 수 없습니다.")
        );
    }

    private Map<String, Object> params() {
        return Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,
                "downStationId", 2L,
                "distance", 7L
        );
    }


}
