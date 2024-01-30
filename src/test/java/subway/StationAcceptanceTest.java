package subway;

import static org.assertj.core.api.Assertions.*;
import static subway.location.enums.Location.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.fixture.StationFixture;
import subway.rest.Rest;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        StationFixture expectedFixture = StationFixture.init().build();
        expectedFixture.actionReturnExtractableResponse();

        // then
        List<String> stationNames = Rest.builder()
            .get(STATIONS.path())
            .jsonPath()
            .getList("name", String.class);
        assertThat(stationNames).containsAnyOf(expectedFixture.getStationName());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 2개를 생성하고, 생성된 2개의 지하철역을 확인한다.")
    @Test
    void createTwoStation() {
        // given
        StationFixture.Builder stationFixture = StationFixture.init();
        String 강남역 = stationFixture.build()
            .actionReturnExtractableResponse()
            .jsonPath()
            .getString("name");
        String 양재역 = stationFixture.stationName("양재역")
            .build()
            .actionReturnExtractableResponse()
            .jsonPath()
            .getString("name");

        // when
        ExtractableResponse<Response> response = Rest.builder().get(STATIONS.path());

        List<String> stationNames = response.jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames).containsAnyOf(강남역, 양재역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("생성된 지하철역을 삭제 한다.")
    @Test
    void deleteStation() {
        // given
        Long stationId = StationFixture.init()
            .build()
            .actionReturnExtractableResponse()
            .jsonPath()
            .getLong("id");

        // when
        Rest.builder().delete(STATIONS.addPath(stationId));

        // then
        List<Long> stationIds = Rest.builder()
            .get(STATIONS.path())
            .jsonPath()
            .getList("" , Long.class);
        assertThat(stationIds).isEmpty();
    }
}
