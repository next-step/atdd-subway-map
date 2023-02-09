package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSectionDeleteRequest;
import subway.dto.LineSectionRequest;
import subway.exception.LineNotFoundException;
import subway.model.Line;
import subway.model.Station;
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
    public LineResponse createLine(LineRequest lineRequest) {
        Station upStation = stationService.showStation(lineRequest.getUpStationId());
        Station downStation = stationService.showStation(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> showLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse showLine(Long id) {
        Line line = findLineById(id);
        return createLineResponse(line);
    }

    @Transactional
    public void modifyLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);
        line.modifyLine(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(stationService.createStationResponse(stationService.showStation(line.getUpStationId()))
                        , stationService.createStationResponse(stationService.showStation(line.getDownStationId())))
        );
    }

    @Transactional
    public void createLineSection(Long id, LineSectionRequest lineSectionRequest) {
        Station upStation = stationService.showStation(lineSectionRequest.getUpStationId());
        Station downStation = stationService.showStation(lineSectionRequest.getDownStationId());
        Line line = findLineById(id);
        line.createLineSection(upStation, downStation);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void deleteLineSection(Long id, LineSectionDeleteRequest lineSectionDeleteRequest) {
        Station station = stationService.showStation(lineSectionDeleteRequest.getStationId());
        Line line = findLineById(id);
        line.deleteLineSection(station);
    }
}
