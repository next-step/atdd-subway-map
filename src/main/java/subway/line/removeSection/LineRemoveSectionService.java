package subway.line.removeSection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Transactional
@Service
public class LineRemoveSectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineRemoveSectionService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineByLineId(lineId);
        Station station = findStationByStationId(stationId);
        line.removeStation(station);
    }

    private Line findLineByLineId(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다. lineId: " + lineId));
    }

    private Station findStationByStationId(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다. stationId: " + stationId));
    }
}
