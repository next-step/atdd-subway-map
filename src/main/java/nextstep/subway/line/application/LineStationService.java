package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineStationCreateRequest;
import nextstep.subway.line.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LineStationService {

    private final LineRepository lineRepository;

    public LineStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void createLineStation(Long lineId, LineStationCreateRequest createRequest) {
        final Line line = lineRepository.findById(lineId)
                .orElseThrow(NotFoundException::new);

        line.addStation(createRequest.getPreStationId(), createRequest.getStationId(), createRequest.getDistance(), createRequest.getDuration());
    }
}
