package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow();
        Section newSection = sectionRepository.save(new Section(upStation, downStation, sectionRequest.getDistance()));

        line.addSection(newSection);
    }

    @Transactional
    public void deleteStation(Long lineId, Long deleteStationId) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        Station station = stationRepository.findById(deleteStationId).orElseThrow();

        Long deleteSectionId = line.deleteStation(station);
        sectionRepository.deleteById(deleteSectionId);
    }
}
