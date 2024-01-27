package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.repository.LineRepository;
import subway.line.repository.domain.Line;
import subway.line.service.dto.LineCreateRequest;
import subway.line.service.dto.LineResponse;
import subway.station.exception.StationNotExistException;
import subway.station.repository.StationRepository;
import subway.station.repository.domain.Station;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineCreateRequest lineCreateRequest) {
        lineCreateRequest.validate();
        final Station upStation = findStation(lineCreateRequest.getUpStationId());
        final Station downStation = findStation(lineCreateRequest.getDownStationId());

        final Line line = lineRepository.save(
                new Line(lineCreateRequest.getName()
                        , lineCreateRequest.getColor()
                        , upStation
                        , downStation
                        , lineCreateRequest.getDistance()));

        return LineResponse.from(line);
    }

    private Station findStation(final Long upStationId) {
        return stationRepository.findById(upStationId).orElseThrow(() -> new StationNotExistException(upStationId));
    }

}
