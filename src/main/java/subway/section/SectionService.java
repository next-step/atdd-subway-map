package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
public class SectionService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {

        Line line = lineRepository.findById(lineId).orElseThrow();
        int currentLineDistance = line.getDistance();
        Section section = sectionRepository.save(sectionRequest.createSection(currentLineDistance));

        Station newSectionDownStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow();

        if(line.isExistStation(newSectionDownStation)){
            throw new IllegalArgumentException("구간의 하행역이 이미 노선에 등록된 역입니다.");
        }

        if(sectionRequest.getUpStationId() != line.getDownStationId()) {
            throw new IllegalArgumentException("구간의 상행역과 노선의 하행역이 일치하지 않습니다.");
        }

        newSectionDownStation.mappingLine(line);
        section.mappingLine(line);
        line.changeDownStationId(sectionRequest);
    }

    @Transactional
    public void deleteStation(Long lineId, Long deleteStationId) {
        Line line = lineRepository.findById(lineId).orElseThrow();

        line.deleteStation(deleteStationId);
        stationRepository.deleteById(deleteStationId);
    }
}
