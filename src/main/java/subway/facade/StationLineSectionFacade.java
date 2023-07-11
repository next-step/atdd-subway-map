package subway.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.service.StationLineSectionService;
import subway.service.request.SectionRequest;

@Service
@Transactional
public class StationLineSectionFacade {

    private final StationLineSectionService sectionService;

    public StationLineSectionFacade(StationLineSectionService sectionService) {
        this.sectionService = sectionService;
    }

    public void addStationLineSection(long id, SectionRequest request) {

        sectionService.validAddSection(id, request.getUpStationId(), request.getDownStationId());

        sectionService.create(
            id,
            request.getUpStationId(),
            request.getDownStationId(),
            request.getDistance()
        );
    }

    public void deleteSection(long lineId, long stationId) {
        sectionService.delete(lineId, stationId);
    }
}
