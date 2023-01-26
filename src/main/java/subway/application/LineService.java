package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(final StationService stationService, final LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest lineRequest) {
        final Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        final Station downStation = stationService.findStationById(lineRequest.getDownStationId());

        final Line line = new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance()
        );
        lineRepository.save(line);
        return new LineResponse(line);
    }

    public List<LineResponse> findAllLine() {
        final List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }
}
