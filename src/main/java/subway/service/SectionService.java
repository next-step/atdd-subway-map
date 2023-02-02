package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.exception.SubwayException;
import subway.exception.statusmessage.SubwayExceptionStatus;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Section findSection(Long id){
        return sectionRepository.findById(id).orElseThrow(
                () -> new SubwayException(SubwayExceptionStatus.SECTION_NOT_FOUND)
        );
    }
}
