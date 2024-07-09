package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import subway.internal.BaseTestSetup;
import subway.internal.StationApiResponseExtractor;
import subway.internal.StationTestApi;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BaseTestSetup {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"강남역", "역삼역", "삼성역"})
    void createStation(String name) {
        // when
        ExtractableResponse<Response> response = StationTestApi.createStation(name);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationApiResponseExtractor.extractNames(StationTestApi.showStations());
        assertThat(stationNames).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @ParameterizedTest
    @CsvSource({"강남역, 잠실역", "역삼역, 삼성역", "삼성역, 선릉역"})
    void showStations(String name1, String name2) {
        // given
        StationTestApi.createStation(name1);
        StationTestApi.createStation(name2);

        // when
        ExtractableResponse<Response> response = StationTestApi.showStations();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> stationNames = StationApiResponseExtractor.extractNames(response);
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsExactlyInAnyOrder(name1, name2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @ParameterizedTest
    @ValueSource(strings = {"강남역", "역삼역", "삼성역"})
    void deleteStation(String name) {
        // given
        long id = StationApiResponseExtractor.Single.extractId(StationTestApi.createStation(name));

        // when
        ExtractableResponse<Response> response = StationTestApi.deleteStation(id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = StationApiResponseExtractor.extractNames(StationTestApi.showStations());
        assertThat(stationNames).doesNotContain(name);
    }
}