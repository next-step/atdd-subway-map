package nextstep.subway.linestation.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.linestation.domain.LineStation;
import nextstep.subway.linestation.dto.LineStationRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineStationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void registerStationToLine(long lineId, LineStationRequest lineStationRequest) {
        final Line line = lineRepository
                .findById(lineId)
                .orElseThrow(RuntimeException::new);
        final LineStation lineStation = toLineStation(lineStationRequest);
        line.registerStation(lineStation);
    }

    private LineStation toLineStation(LineStationRequest lineStationRequest) {
        final Station station = stationRepository
                .findById(Long.parseLong(lineStationRequest.getStationId()))
                .orElseThrow(RuntimeException::new);
        return new LineStation(
                station, null,
                Long.parseLong(lineStationRequest.getDuration()),
                Long.parseLong(lineStationRequest.getDistance())
        );
    }
}
