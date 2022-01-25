package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void validateUpStation(Section section) {
        if (!sections.get(sections.size()-1).getDownStation().getId()
            .equals(section.getUpStation().getId())) {
            throw new IllegalArgumentException("등록하는 구간의 상행역이 기존 최하행역과 맞지 않음.");
        }
    }

    public void validateDownStation(Section section) {
        if (sections.stream()
            .anyMatch(section1 -> (section1.getUpStation().getId()
                .equals(section.getDownStation().getId()))
                || section1.getDownStation().getId().equals(section.getDownStation().getId()))) {
            throw new IllegalArgumentException("등록하는 구간의 하행역이 기존 라인에 등록된 역임.");
        }
    }

    public int size() {
        return sections.size();
    }

    public Section get(int index) {
        return sections.get(index);
    }

    public List<Section> get() {
        return sections;
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }
}
