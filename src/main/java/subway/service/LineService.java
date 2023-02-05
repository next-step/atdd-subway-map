package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
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

    @Transactional
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
        return LineResponse.from(line);
    }

    public LineResponse findLineById(Long id) {
        return createLineResponse(findById(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.updateNameAndColor(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = findById(id);
        lineRepository.deleteById(line.getId());
    }

    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Line line = findById(id);
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = findById(id);
        Station station = stationService.findById(stationId);
        line.deleteSection(station);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}

