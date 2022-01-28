package nextstep.subway.line.application.manager;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.application.manager.StationLineManager;
import org.springframework.stereotype.Component;

@Component
public class StationLineManagerImpl implements StationLineManager {

    private final SectionRepository sectionRepository;

    public StationLineManagerImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean isExistsByStationId(Long stationId) {
        return sectionRepository.existsByUpStationIdOrDownStationId(stationId, stationId);
    }
}
