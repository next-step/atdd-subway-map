package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineStationRequest;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineStationService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineStationService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public void addStation(Long lineId, LineStationRequest lineStationRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 라인이 없습니다:" + lineId));

        validateStationExist(lineStationRequest.getStationId());
        line.addStation(lineStationRequest.toLineStation());
    }

    private void validateStationExist(Long stationId) {
        stationService.findById(stationId)
            .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 지하철역이 없습니다:" + stationId));
    }
}
