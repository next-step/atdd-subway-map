package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.line.web.AddSectionRequest;
import subway.line.web.CreateLineRequest;
import subway.line.web.LineResponse;
import subway.line.web.UpdateLineRequest;
import subway.section.Section;
import subway.station.repository.Station;
import subway.station.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

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
    public LineResponse createLine(CreateLineRequest request) {

        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());
        Section section = new Section(1, request.getDistance(), upStation, downStation);
        Line line = new Line(
                request.getName(),
                request.getColor(),
                section
        );

        Line newLine = lineRepository.save(line);

        return new LineResponse(newLine);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = findLine(id);
        return new LineResponse(line);
    }
    @Transactional
    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = findLine(id);
        line.updateName(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    @Transactional
    public Long addSection(Long id, AddSectionRequest request) {
        Line line = findLine(id);
        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());

        Section section = new Section(line.lastSection().getSequence(), request.getDistance(), upStation, downStation);
        line.appendSection(section);

        lineRepository.saveAndFlush(line);
        return line.lastSection().getId();
    }

    @Transactional
    public void removeSection(Long id, Long sectionId) {
        Line line = findLine(id);
        line.removeSection(sectionId);
    }
}
