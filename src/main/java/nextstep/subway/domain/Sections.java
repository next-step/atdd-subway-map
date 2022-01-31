package nextstep.subway.domain;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
public class Sections {
    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
