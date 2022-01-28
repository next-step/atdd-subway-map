package nextstep.subway.line.application.manager;

import java.util.List;
import java.util.Set;

public interface LineStationManager {

    List<StationData> getAllInStations(Set<Long> stationIds);

    boolean isExistInStations(Set<Long> stationsIds);
}
