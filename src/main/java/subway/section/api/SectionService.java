package subway.section.api;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.global.exception.AlreadyRegisteredException;
import subway.global.exception.SectionMismatchException;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;
import subway.section.SectionRepository;
import subway.section.api.response.SectionResponse;
import subway.section.domain.Section;
import subway.section.presentation.request.SectionCreateRequest;

import javax.persistence.EntityNotFoundException;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public SectionResponse create(Long lineId, SectionCreateRequest request) {

        Line line = lineRepository.findById(lineId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Line Id '%d'를 찾을 수 없습니다.", lineId))
        );

        validateSequence(request, line);
        validateUniqueness(request, line);

        Section section = SectionCreateRequest.toEntity(
                request.getDownStationId(),
                request.getUpStationId(),
                request.getDistance()
        );
        section.addLine(line);
        Section savedSection = sectionRepository.save(section);

        line.getSections().addSection(savedSection);
        line.changeDownStation(request.getDownStationId());
        line.changeDistance(request.getDistance());

        return SectionResponse.of(savedSection);
    }

    private void validateSequence(SectionCreateRequest request, Line line) {
        if (line.getDownStationId() != request.getUpStationId()) {
            throw new SectionMismatchException();
        }
    }

    private void validateUniqueness(SectionCreateRequest request, Line line) {
        if (line.getSections().getStationIds().contains(request.getDownStationId())) {
            throw new AlreadyRegisteredException();
        }
    }
}
