package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.Line;
import subway.repository.LineRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line line = lineRepository.save(toLine(request));
        return toLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(final Long id) {
        final Line line = lineRepository.findById(id).orElse(Line.EMPTY);
        return toLineResponse(line);
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest request) {
        final Optional<Line> optionalLine = lineRepository.findById(id);
        if (optionalLine.isPresent()) {
            final Line line = updateLine(optionalLine.get(), request);
            lineRepository.save(line);
        }
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    private Line toLine(final LineRequest request) {
        return new Line(request.getName(), request.getColor(),
                request.getUpStationId(), request.getDownStationId());
    }

    private LineResponse toLineResponse(final Line line) {
        final StationResponse upStation = stationService.findStationById(line.getUpStationId());
        final StationResponse downStation = stationService.findStationById(line.getDownStationId());
        final List<StationResponse> stations = Arrays.asList(upStation, downStation);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private Line updateLine(final Line line, final LineRequest request) {
        return new Line(line.getId(), request.getName(), request.getColor(),
                line.getUpStationId(), line.getDownStationId());
    }
}
