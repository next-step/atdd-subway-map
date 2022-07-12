package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineSaveRequest;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLines(final LineSaveRequest request) {
        final List<StationResponse> endStations = stationService.findEndStations(List.of(request.getUpStationId(), request.getDownStationId()));

        final Line savedLine = lineRepository.save(new Line(request.getName(),
            request.getColor(), request.getUpStationId(), request.getDownStationId(), new Distance(request.getDistance())));

        return new LineResponse(savedLine.getId(), savedLine.getName(), savedLine.getColor(), endStations);
    }

    public List<LineResponse> getLines() {
        final List<Line> linesList = lineRepository.findAll();

        return linesList.stream()
            .map(line -> new LineResponse(
                line.getId(), line.getName(), line.getColor(),
                stationService.findEndStations(List.of(line.getUpStationId(), line.getDownStationId())))
            ).collect(Collectors.toList());
    }

    public LineResponse getOneLine(final Long id) {
        final Line line = lineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("해당하는 노선을 찾을 수 없습니다."));

        return new LineResponse(
            line.getId(), line.getName(), line.getColor(),
            stationService.findEndStations(List.of(line.getUpStationId(), line.getDownStationId())));
    }

    @Transactional
    public void updateLine(final Long id, final LineUpdateRequest request) {
        final Line line = lineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("해당하는 노선을 찾을 수 없습니다."));

        line.updateNameAndColor(request.getName(), request.getColor());
    }

}
