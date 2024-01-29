package subway.station.acceptance;

import core.AcceptanceTestExtension;
import core.TestConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import subway.common.StationApiHelper;
import subway.station.service.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(AcceptanceTestExtension.class)
public class StationAcceptanceTest {


    public static final String STATION_API_PATH = "/stations";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final String 강남역 = "강남역";
        final ExtractableResponse<Response> response = StationApiHelper.createStation(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(StationResponse.class).getName()).isEqualTo(강남역);

        // then
        final List<String> stationNames = getStationNames(StationApiHelper.fetchStations());
        assertThat(stationNames).containsAnyOf(강남역);
    }

    /**
     * Given 3개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void fetchStationsTest() {
        // given
        final String 지하철역이름 = "지하철역이름";
        final String 새로운지하철역이름 = "새로운지하철역이름";
        final String 또다른지하철역이름 = "또다른지하철역이름";
        final List<String> targetStations = List.of(지하철역이름, 새로운지하철역이름, 또다른지하철역이름);
        targetStations.forEach(StationApiHelper::createStation);

        // when
        final ExtractableResponse<Response> response = StationApiHelper.fetchStations();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final List<String> stationNames = getStationNames(response);
            softly.assertThat(stationNames).containsAll(List.of(지하철역이름, 새로운지하철역이름, 또다른지하철역이름));
        });
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStationTest() {
        // given
        final String 지하철역이름 = "지하철역이름";
        final Long insertedStationId = StationApiHelper.createStation(지하철역이름).jsonPath().getLong("id");

        // when
        final ExtractableResponse<Response> response = StationApiHelper.removeStation(insertedStationId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            final List<String> stationNames = getStationNames(StationApiHelper.fetchStations());
            softly.assertThat(stationNames).doesNotContain(지하철역이름);
        });
    }

    private List<String> getStationNames(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

}
