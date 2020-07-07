package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LineStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void addLineStation(Long lineId, LineStationRequest lineStationRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);

        LineStation lineStation = new LineStation(lineStationRequest.getStationId(), lineStationRequest.getPreStationId(), lineStationRequest.getDistance(), lineStationRequest.getDuration());
        line.addLineStation(lineStation);
    }

}
