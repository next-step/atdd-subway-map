package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.exception.*;

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
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, lineRequest.getDistance()));
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

        if (!line.hasLastStation(upStation)) {
            String lastStationName = line.getLastStation().getName();
            String upStationName = upStation.getName();
            throw new InvalidSectionUpStationException(lastStationName, upStationName);
        }

        if (line.hasStation(downStation)) {
            throw new InvalidSectionDownStationException(downStation.getName());
        }

        line.addSection(new Section(line, upStation, downStation, sectionRequest.getDistance()));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findById(stationId);

        if (line.hasSingleSection()) {
            throw new CannotRemoveUniqueSectionException();
        }

        if (!line.hasLastStation(station)) {
            throw new CannotRemoveNonLastSectionException();
        }

        line.removeSection();
    }
}
