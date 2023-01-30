package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when then
        final String 강남역 = "강남역";
        지하철역을_생성한다(강남역);

        // then
        지하철역_목록에서_조회_가능하다(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void 지하철역_목록_조회() {
        // given
        final List<String> stationNames = List.of("강남역", "잠실역");
        stationNames.forEach(it -> 지하철역을_생성한다(it));

        // when then
        지하철역_목록에서_조회_가능하다(stationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거")
    @Test
    void 지하철역_제거() {
        final String 강남역 = "강남역";
        final String 잠실역 = "잠실역";

        final Long 강남역Id = 지하철역을_생성한다(강남역).body().jsonPath().getLong("id");
        지하철역을_생성한다(잠실역);

        지하철역을_삭제한다(강남역Id);

        지하철역_목록에서_조회_가능하다(잠실역);
        지하철역_목록에서_조회_불가능하다(강남역);
    }

    List<String> 지하철역_이름들을_조회한다() {
        return 지하철역_목록을_조회한다().jsonPath().getList("name", String.class);
    }

    private void 지하철역_목록에서_조회_가능하다(List<String> stationNames) {
        final List<String> createdStationNames = 지하철역_이름들을_조회한다();

        assertThat(createdStationNames)
            .hasSizeGreaterThanOrEqualTo(stationNames.size())
            .containsAll(stationNames);
    }

    private void 지하철역_목록에서_조회_가능하다(String... stationNames) {
        지하철역_목록에서_조회_가능하다(List.of(stationNames));
    }

    private void 지하철역_목록에서_조회_불가능하다(String... stationNames) {
        final List<String> createdStationNames = 지하철역_이름들을_조회한다();

        assertThat(createdStationNames).doesNotContain(stationNames);
    }

}
