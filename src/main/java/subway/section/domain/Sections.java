package subway.section.domain;

import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line_id", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public boolean contains(Station downStation) {
       return sections.contains(downStation);
    }
}
