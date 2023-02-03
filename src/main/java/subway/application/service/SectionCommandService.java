package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.SectionCommandUseCase;
import subway.domain.SectionCreateDto;

@Service
@Transactional
public class SectionCommandService implements SectionCommandUseCase {

    @Override
    public Long createSection(SectionCreateDto lineCreateDto) {
        return null;
    }

}
