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
    public SectionResponse saveSection(SectionRequest sectionRequest) {
        Sections sections = new Sections(sectionRepository.findAll());
        saveValidate(sections, sectionRequest);

        Section section = new Section(
                sectionRequest.getDownStationId(),sectionRequest.getUpStationId(), sectionRequest.getDistance()
        );

        return new SectionResponse(sectionRepository.save(section));
    }

    private void saveValidate(Sections sections, SectionRequest sectionRequest) {
        if (sections.hasSection(sectionRequest.getDownStationId())) {
            throw new IllegalStateException("하행역이 기존 구간에 등록되있는 역입니다.");
        }

        if (!sections.equalsLastSectionDownStationId(sectionRequest.getUpStationId())) {
            throw new IllegalStateException("새로운 구간의 상행선은 해당 노선에 등록되어 있는 하행 종점역이여야 합니다.");
        }
    }

    @Transactional
    public SectionResponse findSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역 정보가 존재하지 않습니다."));
        return createSectionResponse(section);
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
