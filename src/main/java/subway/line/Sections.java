package subway.line;

import javax.persistence.*;
import java.util.List;

@Embeddable
public class Sections {

    private static final long MIN_SECTION_COUNT = 1L;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Section> sections;

    public Sections() {
    }

    public Sections(List<Section> section) {
        this.sections = section;
    }

    public Section findByDownStationId(Long downStationId) {
        return sections.stream()
                .filter(section -> section.getDownStationId().equals(downStationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("구간을 찾을 수 없습니다. (downStationId: %d)", downStationId)));
    }

    public void add(Section section) {
        boolean containsDownStation = sections.stream().anyMatch(storedSection -> storedSection.contains(section.getDownStation()));
        if (containsDownStation) {
            throw new IllegalArgumentException(String.format("새로운 구간의 하행역은 이미 노선에 등록되어있는 역일 수 없습니다. (downStationId: %d)", section.getDownStationId()));
        }

        sections.add(section);
    }

    public void delete(Section section) {
        if (sections.size() <= MIN_SECTION_COUNT) {
            throw new IllegalArgumentException(String.format("구간은 최소 %d개 이상이야 합니다.", MIN_SECTION_COUNT));
        }

        sections.remove(section);
    }
}
