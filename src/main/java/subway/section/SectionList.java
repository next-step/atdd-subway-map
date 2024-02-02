package subway.section;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class SectionList {

    @OneToMany(mappedBy = "line", cascade={CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void deleteSection(Section section) {
        sections.remove(section);
    }
}
