package subway.service;

import org.springframework.stereotype.Service;
import subway.dto.SectionRequest;
import subway.model.Line;
import subway.model.Section;
import subway.model.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import javax.transaction.Transactional;
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

    @Transactional
    public Section createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.findLineById(lineId);
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        Section newSection = new Section.Builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(sectionRequest.getDistance())
                .build();
        newSection.validate(line);
        Section section = sectionRepository.save(newSection);
        line.update(downStation, sectionRequest.getDistance());
        lineService.save(line);
        return section;
    }

    @Transactional
    public void deleteLastSection(Long lineId, Long upStationId) {
        Section lastSection = sectionRepository.findByLineIdAndUpStationId(lineId, upStationId);
        Line line = lineService.findLineById(lineId);
        line.deleteLastSection(lastSection);
        lineService.save(line);
        sectionRepository.delete(lastSection);
    }
}
