package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.fixture.AcceptanceTest;
import subway.fixture.StationFixture;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private final StationFixture stationFixture = new StationFixture();

    @BeforeEach
    public void required() {
        assertThat(stationFixture.모든_지하철역을_조회한다()).isEmpty();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final String name = "강남역";

        // when
        final var response = stationFixture.지하철역을_생성한다(name);

        // then
        assertAll(
                () -> assertThat(response.getId()).isPositive(),
                () -> assertThat(response.getName()).isEqualTo(name)
        );
        assertThat(stationFixture.지하철역을_조회한다(name)).isPresent();
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("모든 지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        final List<String> names = List.of("강남역", "역삼역");

        // given
        stationFixture.지하철역을_생성한다(names);

        // when
        final var responses = stationFixture.모든_지하철역을_조회한다();

        // then
        assertThat(responses)
                .extracting(StationResponse::getName)
                .hasSameElementsAs(names);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final String name = "강남역";

        // given
        final Long stationId = stationFixture.지하철역을_생성한다(name).getId();

        // when
        stationFixture.지하철역을_제거한다(stationId);

        // then
        assertThat(stationFixture.모든_지하철역을_조회한다()).isEmpty();
    }
}
