package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.NotFoundLineException;
import nextstep.subway.exception.NotFoundSectionException;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.domain.Section.validateForDelete;
import static nextstep.subway.domain.Section.validateForSave;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(final SectionRepository sectionRepository, final LineRepository lineRepository, final StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(final SectionRequest request, final Long id) {
        final Line line = findLineById(id);
        final Station upStation = findStationById(request.getUpStationId());
        final Station downStation = findStationById(request.getDownStationId());

        final List<Section> sections = line.getSections();
        if(!sections.isEmpty()) {
            validateForSave(sections, request.getUpStationId(), request.getDownStationId());
        }

        final Section section = Section.of(line, upStation, downStation, request);
        final Section createdSection = sectionRepository.save(section);
        return SectionResponse.of(createdSection);
    }

    public void deleteSectionById(final Long lineId, final Long sectionId) {
        final Line line = findLineById(lineId);
        final Section section = findSectionById(sectionId);
        validateForDelete(line.getSections(), section.getDownStation().getId());
        sectionRepository.deleteById(sectionId);
    }

    private Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(NotFoundStationException::new);
    }

    private Section findSectionById(final Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(NotFoundSectionException::new);
    }
}
