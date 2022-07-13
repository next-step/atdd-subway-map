package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line")
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public Sections(Section section) {
        sections.add(section);
    }

    public void addSection(Section section) {
        sections.add(section);
    }
}
