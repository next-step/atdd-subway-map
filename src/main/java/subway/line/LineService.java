package subway.line;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CannotRemoveLineSectionException;
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundLineSectionException;
import subway.line.section.LineSection;
import subway.section.Section;
import subway.section.SectionRequest;
import subway.section.SectionResponse;
import subway.section.SectionService;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    private final SectionService sectionService;


    @Transactional
    public Line saveLine(LineRequest lineRequest) {
        Line savedLine = createAndSaveLine(lineRequest);

        Section section = sectionService.registerSection(SectionRequest.of(lineRequest));

        savedLine.addSection(section);

        return savedLine;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAllWithDefault().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<LineResponse> findById(Long id) {
        return lineRepository.findById(id)
            .map(LineResponse::of);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void modifyLine(Long id, LineModifyRequest lineModifyRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(NotFoundLineException::new);

        line.modify(lineModifyRequest);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void deleteLine(Long id) {
        Line findLine = lineRepository.findById(id)
            .orElseThrow(NotFoundLineException::new);

        lineRepository.delete(findLine);
    }

    @Transactional
    public SectionResponse addLineSection(Long lineId, SectionRequest sectionRequest) {
        Line line = findLineById(lineId);

        Section section = sectionService.searchSection(
            sectionRequest.getUpStationId(),
            sectionRequest.getDownStationId()
        ).orElseGet(() ->
            sectionService.registerSection(sectionRequest)
        );

        line.addSection(section);

        return SectionResponse.of(section);
    }

    @Transactional
    public void removeLineSection(Long lineId, Long downStationId) {
        Line line = findLineById(lineId);

        Preconditions.checkState(line.getLineSectionCount() > 1,
            "There is only one section");

        LineSection lineSection = line.getLastLineSection()
            .orElseThrow(NotFoundLineSectionException::new);

        if (!Objects.equals(lineSection.getDownStationId(), downStationId)) {
            throw new CannotRemoveLineSectionException();
        }

        line.removeLineSection(lineSection);
    }

    private Line createAndSaveLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor());

        return lineRepository.save(line);
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(NotFoundLineException::new);
    }
}
