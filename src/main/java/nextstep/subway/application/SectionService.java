package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.ui.dto.section.CreateSectionRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public void createSection(CreateSectionRequest request){

    }
}
