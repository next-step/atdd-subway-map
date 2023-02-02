package subway.line.section;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CannotRemoveLineSectionException;
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundLineSectionException;
import subway.line.Line;
import subway.line.LineRepository;
import subway.section.Section;
import subway.section.SectionService;

@Service
@RequiredArgsConstructor
public class LineSectionService {
    private final LineRepository lineRepository;

    private final LineSectionRepository lineSectionRepository;

    private final SectionService sectionService;

    @Transactional
    public LineSection registerLineSection(Long lineId, Long upStationId, Long downStationId, int distance) {
        Line line = lineRepository.findByIdWithLineSections(lineId)
            .orElseThrow(NotFoundLineException::new);

        Section savedSection = sectionService.searchSection(upStationId, downStationId).orElse(
            sectionService.registerSection(upStationId, downStationId, distance)
        );

        return createAndSaveLineSection(line, savedSection);
    }

    @Transactional(readOnly = true)
    public List<LineSection> findAllSections(Long lineId) {
        return lineSectionRepository.findAllByLineId(lineId);
    }

    @Transactional
    public void removeLineSection(Long lineId, Long downStationId) {
        Line line = lineRepository.findByIdWithLineSections(lineId)
            .orElseThrow(NotFoundLineException::new);

        LineSection lineSection = line.getLastLineSection();

        if (!Objects.equals(lineSection.getDownStationId(), downStationId)) {
            throw new CannotRemoveLineSectionException();
        }

        lineSectionRepository.delete(lineSection);
    }

    private LineSection createAndSaveLineSection(Line line, Section section) {
        LineSection lineSection = new LineSection(line, section);

        LineSection savedLineSection = lineSectionRepository.save(lineSection);

        line.addLineSection(savedLineSection);

        return savedLineSection;
    }

    public LineSection findLineSection(Long lineId, Long sectionId) {
        return lineSectionRepository.findById(sectionId)
            .orElseThrow(NotFoundLineSectionException::new);
    }
}
