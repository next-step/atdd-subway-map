package nextstep.subway.station.domain;

import nextstep.subway.section.domain.manager.SectionStationManager;
import nextstep.subway.section.domain.manager.StationData;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SectionStationManagerImpl implements SectionStationManager {

    private final StationRepository stationRepository;

    public SectionStationManagerImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public List<StationData> getAllInStations(Set<Long> stationIds) {
        return stationRepository.findAllByIdIn(stationIds).stream()
                .map(StationData::of).collect(Collectors.toList());
    }

    @Override
    public boolean isExistInStations(Long upStationId, Long downStationId) {
        Set<Long> ids = new HashSet();
        ids.add(upStationId);
        ids.add(downStationId);
        return stationRepository.findAllByIdIn(ids).size() == 2;
    }
}
