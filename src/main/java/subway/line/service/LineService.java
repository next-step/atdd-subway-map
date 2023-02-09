package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.section.dto.SectionRequest;
import subway.line.repository.Section;
import subway.station.repository.Station;
import subway.station.service.StationService;

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
        Line entity = toLineEntity(lineRequest);
        Line line = lineRepository.save(entity);

        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NullPointerException::new);
    }

    public LineResponse getLine(Long id) {
        return LineResponse.of(findLine(id));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = findLine(id);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Section section = toSectionEntity(
                sectionRequest.getDownStationId(),
                sectionRequest.getUpStationId(),
                sectionRequest.getDistance()
        );

        Line line = findLine(id);
        line.addSection(section);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = findLine(id);
        line.removeSection(stationId);
    }

    private Line toLineEntity(LineRequest lineRequest) {
        Section section = toSectionEntity(
                lineRequest.getDownStationId(),
                lineRequest.getUpStationId(),
                lineRequest.getDistance()
        );

        return new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                section
        );
    }

    private Section toSectionEntity(Long downStationId, Long upStationId, int distance) {
        Station downStation = stationService.findStation(downStationId);
        Station upStation = stationService.findStation(upStationId);

        return new Section(
                downStation,
                upStation,
                distance
        );
    }
}
