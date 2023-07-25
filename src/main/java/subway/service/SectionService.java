package subway.service;

import org.springframework.stereotype.Service;
import subway.dto.SectionRequest;
import subway.model.Line;
import subway.model.Section;
import subway.model.Station;
import subway.repository.SectionRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, StationService stationService, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public List<Section> findAllSectionsByLineId(Long lineId) {
        return lineService.findLineById(lineId)
                .getSections();
    }

    @Transactional
    public Section createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.findLineById(lineId);
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        Section newSection = line.createSection(upStation, downStation, sectionRequest.getDistance());
        Section section = sectionRepository.save(newSection);
        line.addSection(section);
        return section;
    }

    @Transactional
    public void deleteLastSection(Long lineId, Long upStationId) {
        Line line = lineService.findLineById(lineId);
        Station upStation = stationService.findStationById(upStationId);
        line.deleteLastSection(upStation);
    }
}
