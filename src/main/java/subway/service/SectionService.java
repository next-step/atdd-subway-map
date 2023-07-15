package subway.service;

import org.springframework.stereotype.Service;
import subway.dto.SectionRequest;
import subway.model.Section;
import subway.model.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, StationService stationService, LineRepository lineRepository, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public List<Section> findAllSectionsByLineId(Long lineId) {
        return sectionRepository.findByLineId(lineId);
    }

    public Section createSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        Section newSection = new Section.Builder()
                .lineId(lineId)
                .upStation(upStation)
                .downStation(downStation)
                .distance(sectionRequest.getDistance())
                .build();
        Section section = sectionRepository.save(newSection);
        lineService.addSectionAndSave(lineId, section);
        return section;
    }
}
