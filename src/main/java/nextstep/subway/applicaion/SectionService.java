package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.line.Section;
import nextstep.subway.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService {
    private final LineRepository lineRepository;

    public SectionService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public SectionResponse saveSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException("존재하지 않는 노선에는 구간을 추가할 수 없습니다.")
        );

        Section section = new Section(id, sectionRequest.getUpStationId(),
            sectionRequest.getDownStationId(), sectionRequest.getDistance());

        line.getSections().addSection(section);

        return new SectionResponse(id, section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }
}
