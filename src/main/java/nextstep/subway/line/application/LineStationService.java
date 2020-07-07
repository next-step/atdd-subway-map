package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineNotFoundException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LineStationService {
    public static final String LINE_NOT_FOUND = "지하철 노선을 찾을 수 없습니다.";
    private final LineRepository lineRepository;

    public LineStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineStation appendStation(Long lineId, LineStationRequest lineStationRequest) {
        Optional<Line> findLine = lineRepository.findById(lineId);

        if (findLine.isPresent()) {
            LineStation lineStation = lineStationRequest.toLineStation();
            findLine.get().addLineStation(lineStation);
            return lineStation;
        }
        throw new LineNotFoundException(LINE_NOT_FOUND);
    }
}
