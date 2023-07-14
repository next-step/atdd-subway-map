package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import javax.transaction.Transactional;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository1) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository1;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        line.addSections(Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(sectionRequest.getDistance()).
                build());
    }

    @Transactional
    public void removeSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        Station station = stationRepository.findById(stationId).get();

        line.removeSections(station);
    }
}
