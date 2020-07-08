package nextstep.subway.linestation.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.linestation.application.exception.StationNotFoundException;
import nextstep.subway.linestation.domain.LineStation;
import nextstep.subway.linestation.dto.LineStationRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        final Long stationId = lineStationRequest.getStationId();
        final boolean exist = stationRepository.existsById(stationId);
        if (!exist) {
            throw new StationNotFoundException("No such station on database!");
        }

        final LineStation lineStation = toLineStation(lineStationRequest);
        line.registerStation(lineStation);
    }

    @Transactional(readOnly = true)
    public List<LineStationResponse> getStationsInLine(long lineId) {
        final Line line = lineRepository
                .findById(lineId)
                .orElseThrow(RuntimeException::new);
        return toLineStationResponse(line.getStationsInOrder());
    }

    @Transactional
    public void removeStationFromLine(long lineId, long stationId) {
        final Line line = lineRepository
                .findById(lineId)
                .orElseThrow(RuntimeException::new);

        final Station station = stationRepository
                .findById(stationId)
                .orElseThrow(StationNotFoundException::new);

        line.removeStation(station);
    }

    private List<LineStationResponse> toLineStationResponse(List<LineStation> lineStations) {
        return lineStations.stream()
                .map(lineStation -> new LineStationResponse(
                        StationResponse.of(lineStation.getStation()),
                        Optional.ofNullable(lineStation.getPreStation()).map(Station::getId).orElse(null),
                        lineStation.getDistance(),
                        lineStation.getDuration()
                ))
                .collect(Collectors.toList());
    }

    private LineStation toLineStation(LineStationRequest lineStationRequest) {
        final Station station = stationRepository
                .findById(lineStationRequest.getStationId())
                .orElseThrow(RuntimeException::new);

        final Long preStationId = lineStationRequest.getPreStationId();
        Station preStation = null;
        if (preStationId != null) {
            preStation = stationRepository
                    .findById(preStationId)
                    .orElseThrow(RuntimeException::new);
        }

        return lineStationRequest.toLineStation(station, preStation);
    }
}
