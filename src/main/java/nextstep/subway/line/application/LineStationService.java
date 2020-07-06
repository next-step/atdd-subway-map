package nextstep.subway.line.application;

import javassist.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.station.domain.StationRepository;
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

    public LineStation appendStation(LineStationRequest lineStationRequest) {
        Optional<Line> findLine = lineRepository.findById(lineStationRequest.getStationId());

        if (findLine.isPresent()) {
            LineStation lineStation = lineStationRequest.toLineStation();
            findLine.get().appendStation(lineStation);
            return lineStation;
        }
        throw new RuntimeException("지하철 노선을 찾을 수 없습니다.");
    }
}
