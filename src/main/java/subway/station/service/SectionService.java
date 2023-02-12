package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.service.dto.SectionSaveRequest;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineService lineService;

    public SectionService(LineService lineService) {
        this.lineService = lineService;
    }

    @Transactional
    public void save(Long id, SectionSaveRequest sectionSaveRequest) {
        lineService.addSection(id, sectionSaveRequest.getUpStationId(), sectionSaveRequest.getDownStationId(), sectionSaveRequest.getDistance());
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        lineService.deleteSection(lineId, stationId);
    }
}
