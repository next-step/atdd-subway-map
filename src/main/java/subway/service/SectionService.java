package subway.service;

import org.springframework.stereotype.Service;
import subway.model.Section;
import subway.repository.SectionRepository;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAllSectionsByLineId(Long lineId) {
        return sectionRepository.findByLineId(lineId);
    }
}
