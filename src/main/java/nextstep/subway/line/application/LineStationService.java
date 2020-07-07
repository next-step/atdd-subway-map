package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineStationRequest;
import org.springframework.stereotype.Service;

@Service
public class LineStationService {

    private LineRepository lineRepository;

    public LineStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void addStation(Long lineId, LineStationRequest lineStationRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 라인이 없습니다"));

        line.addStation(lineStationRequest.toLineStation());
    }
}
