package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class SectionCommandService {

    private final SectionRepository sectionRepository;

    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

}
