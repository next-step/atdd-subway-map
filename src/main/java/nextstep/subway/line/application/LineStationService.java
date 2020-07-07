package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.application.StationNotFoundException;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LineStationService {
    private static final String LINE_NOT_FOUND = "지하철 노선을 찾을 수 없습니다.";
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineStationResponse appendStation(Long lineId, LineStationRequest lineStationRequest) {
        Optional<Line> findLine = lineRepository.findById(lineId);

        if (findLine.isPresent()) {
            LineStation lineStation = lineStationRequest.toLineStation();
            findLine.get().addLineStation(lineStation);

            Station station = stationRepository.findById(lineStation.getStationId())
                    .orElseThrow(StationNotFoundException::new);

            return LineStationResponse.of(lineStation, StationResponse.of(station));

        }
        throw new LineNotFoundException();
    }
}
