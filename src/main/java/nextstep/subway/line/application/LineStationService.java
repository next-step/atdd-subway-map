package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class LineStationService {
    private final LineRepository lineRepository;

    public LineStationService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineStation addStation(Long lineId, LineStationRequest lineStationRequest) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(EntityNotFoundException::new);
        final LineStation station = lineStationRequest.toLineStation();
        line.addStation(station);
        return station;
    }
}
