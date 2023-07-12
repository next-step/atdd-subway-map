package subway.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.service.SectionService;
import subway.service.request.SectionRequest;

@Service
@Transactional
public class SectionFacade {

    private final SectionService sectionService;

    public SectionFacade(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    public void addStationLineSection(long id, SectionRequest request) {

        sectionService.add(
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
