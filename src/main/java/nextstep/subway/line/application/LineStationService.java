package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
@Transactional
public class LineStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineStationResponse addStation(Long lineId, LineStationRequest lineStationRequest) {
        checkAreStationsExist(lineStationRequest);

        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(EntityNotFoundException::new);
        LineStation lineStation = lineStationRequest.toLineStation();
        line.addStation(lineStation);

        final Station station = this.stationRepository.findById(lineStation.getStationId())
                .orElseThrow(EntityNotFoundException::new);
        return LineStationResponse.of(lineStation, station);
    }

    private void checkAreStationsExist(LineStationRequest lineStationRequest) {
        if (!this.stationRepository.existsById(lineStationRequest.getStationId())) {
            throw new EntityNotFoundException();
        }

        if (Objects.isNull(lineStationRequest.getPreStationId())) {
            return;
        }

        if (!this.stationRepository.existsById(lineStationRequest.getPreStationId())) {
            throw new EntityNotFoundException();
        }
    }
}
