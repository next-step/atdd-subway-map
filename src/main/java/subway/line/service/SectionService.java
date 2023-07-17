package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.request.SectionRequest;
import subway.line.dto.response.SectionResponse;
import subway.line.repository.SectionRepository;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

//    public SectionResponse createSection(long lineId, SectionRequest sectionRequest) {
//
//    }

}
