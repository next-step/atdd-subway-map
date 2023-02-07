package subway.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.api.dto.SectionRequest;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private LineService lineService;

    public SectionService(LineService lineService) {
        this.lineService = lineService;
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        lineService.addSection(lineId, sectionRequest);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        lineService.deleteSection(lineId, stationId);
    }
}
