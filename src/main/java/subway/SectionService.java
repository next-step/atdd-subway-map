package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public SectionResponse findSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역 정보가 존재하지 않습니다."));
        return new SectionResponse(section);
    }

    @Transactional
    public void deleteStationsById(Long id) {
        Sections sections = new Sections(sectionRepository.findAll());
        deleteValidate(sections, id);

        sectionRepository.deleteById(id);
    }

    private void deleteValidate(Sections sections, Long deleteSectionId) {
        if (sections.getSectionCount() <= 1) {
            throw new IllegalStateException("구간이 하나일 경우 삭제할 수 없습니다.");
        }

        if (!sections.equalsLastSection(deleteSectionId)) {
            throw new IllegalStateException("마지막 구간만 삭제할 수 있습니다.");
        }
    }

    public List<SectionResponse> findAllSections() {
        return sectionRepository.findAll().stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section);
    }
}
