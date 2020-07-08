package nextstep.subway.line.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineStationCreateRequest;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@Transactional
public class LineStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void createLineStation(Long lineId, LineStationCreateRequest createRequest) {
        validateStationExists(createRequest.getStationId(), createRequest.getPreStationId());
        final Line line = findLineById(lineId);

        line.addStation(createRequest.getPreStationId(), createRequest.getStationId(), createRequest.getDistance(), createRequest.getDuration());
    }

    public void removeStation(Long lineId, Long stationId) {
        final Line line = findLineById(lineId);
        line.removeStationByStationId(stationId);
    }

    private void validateStationExists(Long stationId, Long preStationId) {
        Stream.of(stationId, preStationId)
                .filter(Objects::nonNull)
                .map(stationRepository::existsById)
                .filter(isExists -> isExists)
                .findAny()
                .orElseThrow(() -> new NotFoundException("지하철역이 존재하지 않습니다."));
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(NotFoundException::new);
    }
}
