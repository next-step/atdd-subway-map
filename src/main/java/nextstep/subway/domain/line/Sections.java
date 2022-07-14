package nextstep.subway.domain.line;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Section> sections = new LinkedList<>();

    protected Sections() {/*no-op*/}

    public Sections(final Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }

    public boolean addSection(Section addSection) {
        if (getSections().stream()
            .anyMatch(section -> Objects.equals(section.getUpStationId(), addSection.getDownStationId())
                || Objects.equals(section.getDownStationId(), addSection.getDownStationId()))) {
            throw new IllegalArgumentException("등록하는 구간의 하행 종점역이 노선에 포함되는 역이 될 수 없습니다.");
        }

        if (!getSections().get(sections.size() - 1).getDownStationId().equals(addSection.getUpStationId())) {
            throw new IllegalArgumentException("등록하는 구간의 상행역이 노선의 하행 종점역이어야 합니다.");
        }

        return sections.add(addSection);
    }

    public Section deleteSection(Long downStationId) {
        if (getSections().size() == 1) {
            throw new IllegalStateException("노선 안에 구간이 하나 뿐입니다.");
        }

        if (!getSections().get(sections.size() - 1).getDownStationId().equals(downStationId)) {
            throw new IllegalArgumentException("하행 종점역만 삭제할 수 있습니다.");
        }
        return sections.remove(sections.size() - 1);
    }
}
