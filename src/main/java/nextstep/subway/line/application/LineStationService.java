package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.exception.DuplicateStationInLineException;
import nextstep.subway.line.exception.NonExistStationInLineException;
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
        Long stationId = lineStationRequest.getStationId();
        validateNonExistStation(stationId);

        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);

        validateDuplicateStation(line, stationId);

        LineStation lineStation = new LineStation(lineStationRequest.getStationId(), lineStationRequest.getPreStationId(), lineStationRequest.getDistance(), lineStationRequest.getDuration());
        line.addLineStation(lineStation);
    }

    private void validateNonExistStation(Long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new NonExistStationInLineException();
        }
    }

    private void validateDuplicateStation(Line line, Long stationId) {
        line.getLineStations()
            .stream()
            .filter(it -> it.getStationId() == stationId)
            .findAny()
            .ifPresent(it -> {
                throw new DuplicateStationInLineException();
            });
    }

}
