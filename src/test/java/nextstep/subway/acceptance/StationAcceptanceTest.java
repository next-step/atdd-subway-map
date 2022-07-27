package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.utils.DatabaseInitializer;
import nextstep.subway.acceptance.utils.StationAcceptanceTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BaseTest {

    public static final String STATION_NAME1 = "강남역";
    public static final String STATION_NAME2 = "삼성역";
    public static final String STATION_NAME3 = "노원역";
    public static final String STATION_NAME4 = "중계역";

    private final StationAcceptanceTestUtils stationAcceptanceTestUtils = new StationAcceptanceTestUtils();

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @AfterEach
    public void initializeTables() {
        databaseInitializer.execute();
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
        ExtractableResponse<Response> response = stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = stationAcceptanceTestUtils.지하철_역_목록_조회()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(STATION_NAME1);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME1);
        stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME1);

        // when
        ExtractableResponse<Response> response = stationAcceptanceTestUtils.지하철_역_목록_조회();

        // then
        assertThat(response.jsonPath().getList("name").size()).isEqualTo(2);
        assertThat(response.jsonPath().getList("name")).isEqualTo(Arrays.asList(STATION_NAME1, STATION_NAME1));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> toDeleteStation = stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME1);
        Long id = toDeleteStation.jsonPath().getLong("id");
        stationAcceptanceTestUtils.지하철_역_생성(STATION_NAME2);

        // when
        stationAcceptanceTestUtils.지하철_역_제거(id);

        // then
        ExtractableResponse<Response> response = stationAcceptanceTestUtils.지하철_역_목록_조회();
        assertThat(response.jsonPath().getList("id")
                .stream()
                .filter(stationId -> stationId == id)
                .count())
                .isEqualTo(0);
    }
}