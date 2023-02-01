package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();


    public void add(Section section) {
        this.sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Section getLast() {
        return sections.get(sections.size() - 1);
    }

    public boolean lessThanTwo() {
        return sections.size() < 2;
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public boolean canAddSection(Section addSection) {

        return this.getLast().isEqualDownStation(addSection)
                && !sections.stream()
                .anyMatch(section -> section.hasStation(addSection.getDownStation()));
    }
}
