package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationDeleteRequest;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.application.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineStationResponse appendStation(Long lineId, LineStationRequest lineStationRequest) {
        Line findLine = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);

        LineStation lineStation = lineStationRequest.toLineStation();
        findLine.addLineStation(lineStation);

        Station station = stationRepository.findById(lineStation.getStationId())
                .orElseThrow(StationNotFoundException::new);
        return LineStationResponse.of(lineStation, StationResponse.of(station));
    }

    public void removeStation(Long lineId, Long stationId) {
        Line findLine = lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);

        findLine.removeStation(stationId);
    }
}
