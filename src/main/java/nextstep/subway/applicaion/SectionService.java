package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.NotFoundLineException;
import nextstep.subway.exception.NotFoundStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.domain.Section.validateForSave;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(final SectionRequest request, Long id) {
        final Line line = findLineById(id);
        final Station upStation = findStationById(request.getUpStationId());
        final Station downStation = findStationById(request.getDownStationId());

        if(!line.getSections().isEmpty()) {
            validateForSave(line.getSections(), request.getUpStationId(), request.getDownStationId());
        }

        final Section section = Section.of(line, upStation, downStation, request);
        final Section createdSection = sectionRepository.save(section);
        return SectionResponse.of(createdSection);
    }

    private Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(NotFoundStationException::new);
    }
}
