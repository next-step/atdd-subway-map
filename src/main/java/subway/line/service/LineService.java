package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.section.dto.SectionRequest;
import subway.section.repository.Section;
import subway.section.service.SectionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final SectionService sectionService;

    public LineService(LineRepository lineRepository, SectionService sectionService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
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
        Section section = sectionService.createSection(sectionRequest);
        Line line = findLine(id);
        line.addSection(section);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = findLine(id);
        Long removeSectionId = line.removeSection(stationId);
        sectionService.deleteSection(removeSectionId);
    }

    private Line toLineEntity(LineRequest lineRequest) {
        Section section = sectionService.createSection(toSectionRequest(lineRequest));

        return new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                section
        );
    }

    private SectionRequest toSectionRequest(LineRequest lineRequest) {
        return new SectionRequest(
                lineRequest.getDownStationId(),
                lineRequest.getUpStationId(),
                lineRequest.getDistance()
        );
    }
}
