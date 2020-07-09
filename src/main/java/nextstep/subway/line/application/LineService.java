package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public Line saveLine(LineRequest request) {
        return lineRepository.save(request.toLine());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 라인이 없습니다:" + lineId));

        Map<Long, StationResponse> stationResponses = stationService.findAllById(getStationIds(line)).stream()
                .collect(Collectors.toMap(StationResponse::getId, Function.identity()));

        List<LineStationResponse> lineStationResponses = getLineStationResponses(line, stationResponses);
        return LineResponse.of(line, lineStationResponses);
    }

    private List<Long> getStationIds(Line line) {
        return line.getLineStations().getContent().stream()
                    .map(LineStation::getStationId)
                    .collect(Collectors.toList());
    }

    private List<LineStationResponse> getLineStationResponses(Line line, Map<Long, StationResponse> stationResponses) {
        return line.getLineStations().getContentInOrder().stream()
                .map(lineStation -> LineStationResponse.of(stationResponses.get(lineStation.getStationId()), lineStation))
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
