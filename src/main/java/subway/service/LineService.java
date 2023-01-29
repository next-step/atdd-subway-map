package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(
                new Line(lineRequest.getName()
                        , lineRequest.getColor()
                        , stationService.findById(lineRequest.getUpStationId())
                        , stationService.findById(lineRequest.getDownStationId())
                        , lineRequest.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId()
                , line.getName()
                , line.getColor()
                , List.of(line.getUpStation(), line.getDownStation()));
    }

    public LineResponse findLineById(Long id) {
        return createLineResponse(lineRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
    }
}
