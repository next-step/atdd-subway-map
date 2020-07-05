package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LineStationService {
    private final LineRepository lineRepository;

    public LineStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void appendStation(LineStationRequest lineStationRequest) {
        Optional<Line> findLine = lineRepository.findById(lineStationRequest.getStationId());

        if (findLine.isPresent()) {
            LineStation lineStation = new LineStation(lineStationRequest.getStationId(), lineStationRequest.getPreStationId(),
                    lineStationRequest.getDistance(), lineStationRequest.getDuration());
            findLine.get().appendStation(lineStation);
        }

    }
}
