package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static nextstep.subway.provider.StationProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
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
        final ExtractableResponse<Response> response = 지하철역_등록_요청("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());

        // then
        final ExtractableResponse<Response> getStationsResponse = 지하철역_목록_조회_요청();
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).hasSize(1);
        assertThat(stationNames).contains("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회합니다.")
    @Test
    void getStations() throws Exception {
        // given
        지하철역_등록_요청(List.of("강남역", "역삼역"));

        // when
        final ExtractableResponse<Response> getStationsResponse = 지하철역_목록_조회_요청();

        // then
        assertThat(getStationsResponse.statusCode()).isEqualTo(OK.value());

        // then
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsAll(List.of("강남역", "역삼역"));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제합니다.")
    @Test
    void deleteStations() throws Exception {
        // given
        final ExtractableResponse<Response> createStationResponse = 지하철역_등록_요청("강남역");
        final Long savedStationId = createStationResponse.jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> deleteStationResponse = 지하철역_삭제_요청(savedStationId);

        // then
        assertThat(deleteStationResponse.statusCode()).isEqualTo(NO_CONTENT.value());

        // then
        final ExtractableResponse<Response> getStationsResponse = 지하철역_목록_조회_요청();
        final List<String> stationNames = getStationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).hasSize(0);
    }
}