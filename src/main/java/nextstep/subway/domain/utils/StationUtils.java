package nextstep.subway.domain.utils;

import java.util.Objects;

public class StationUtils {
    public static boolean isSameStation(Long stationId1, Long stationId2) {
        return Objects.equals(stationId1, stationId2);
    }

}
