package subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class StationAssert {

    public static void 역_생성_검증(final List<String> stationNames, final String expected) {
        assertThat(stationNames).containsAnyOf(expected);
    }

    public static void 역_목록_조회_검증(final String[] expectedStationNames, final List<String> stationNames) {
        assertThat(stationNames).hasSize(expectedStationNames.length).contains(expectedStationNames);
    }

    public static void 역_삭제_검증(final long deleteStationId, final List<Long> stationIds) {
        assertThat(stationIds).doesNotContain(deleteStationId);
    }
}
