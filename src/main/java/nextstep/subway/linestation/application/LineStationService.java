package nextstep.subway.linestation.application;

import nextstep.subway.linestation.domain.LineStationRepository;
import nextstep.subway.linestation.dto.LineStationRequest;
import org.springframework.stereotype.Service;

@Service
public class LineStationService {

    private final LineStationRepository repository;

    public LineStationService(LineStationRepository repository) {
        this.repository = repository;
    }

    public void registerStationToLine(long lineId, LineStationRequest lineStationRequest) {

    }
}
