package subway.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.exception.CannotCreateLineException;
import subway.exception.LineNotFoundException;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Long upStationId = lineRequest.getUpStationId();
        Long downStationId = lineRequest.getDownStationId();

        if (Objects.equals(upStationId, downStationId)) {
            throw new CannotCreateLineException();
        }

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        Station upStation = stationService.findById(upStationId);
        Station downStation = stationService.findById(downStationId);
        line.addSection(upStation, downStation, lineRequest.getDistance());
        return new LineResponse(line);
    }

    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        return new LineResponse(line);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new LineNotFoundException(id));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Line line = findLineById(id);
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findById(stationId);
        line.removeSection(station);
    }
}
