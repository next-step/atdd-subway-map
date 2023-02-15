package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.service.dto.SectionRequest;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineService lineService;

    public SectionService(LineService lineService) {
        this.lineService = lineService;
    }

    @Transactional
    public void save(Long id, SectionRequest sectionRequest) {
        lineService.addSection(id, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        lineService.deleteSection(lineId, stationId);
    }
}
