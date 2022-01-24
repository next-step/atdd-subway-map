package nextstep.subway.section.application.manager;

import java.util.List;
import java.util.Set;

public interface SectionStationManager {
    List<StationData> getAllInStations(Set<Long> stationIds);

    boolean existInStations(Long upStationId, Long downStationId);
}
