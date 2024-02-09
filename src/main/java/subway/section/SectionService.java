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

    public SectionService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).orElseThrow();

        Station station = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow();

        if(line.isExistStation(station)){
            throw new IllegalArgumentException("구간의 하행역이 이미 노선에 등록된 역입니다.");
        }

        if(sectionRequest.getUpStationId() == line.getDownStationId()) {
            station.mappingLine(line);

            line.changeDownStationId(sectionRequest.getDownStationId());
        }
    }
}
