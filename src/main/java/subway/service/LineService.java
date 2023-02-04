package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;
import subway.dto.section.SectionRequest;
import subway.exception.NotFoundException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());
        Section section = new Section(upStation, downStation, lineRequest.getDistance());

        Line newLine = new Line(lineRequest.getName(), lineRequest.getColor(), section);
        lineRepository.save(newLine);
        return LineResponse.from(newLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findLineById(Long id) {
        return LineResponse.from(findLine(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findLine(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        Line line = findLine(id);
        lineRepository.delete(line);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findStation(sectionRequest.getUpStationId());
        Station downStation = stationService.findStation(sectionRequest.getDownStationId());
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());

        Line line = findLine(lineId);
        line.addSection(section);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = findLine(id);
        line.deleteSection(stationId);
    }

    private Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "번 노선을 찾을 수 없습니다."));
    }
}
