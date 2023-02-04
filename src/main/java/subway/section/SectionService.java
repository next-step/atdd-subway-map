package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.DtoConverter;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final LineRepository lineRepository;

    private final DtoConverter dtoConverter;
    private final StationRepository stationRepository;

    public SectionService(DtoConverter dtoConverter, LineRepository lineRepository,
                          StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.dtoConverter = dtoConverter;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        Line line = findLineEntity(lineId);
        Station lastStation = findStationEntity(sectionCreateRequest.getUpStationId());
        Station newStation = findStationEntity(sectionCreateRequest.getDownStationId());
        line.addStation(lastStation, newStation, sectionCreateRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        line.deleteStation(stationId);
    }

    private Line findLineEntity(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 지하철 노선 번호 입니다"));
    }

    private Station findStationEntity(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 지하철 번호 입니다"));
    }

}
