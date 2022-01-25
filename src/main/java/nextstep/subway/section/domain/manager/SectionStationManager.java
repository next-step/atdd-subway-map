package nextstep.subway.section.domain.manager;

import java.util.List;
import java.util.Set;

public interface SectionStationManager {

    List<StationData> getAllInStations(Set<Long> stationIds);

    boolean isExistInStations(Long upStationId, Long downStationId);
}
