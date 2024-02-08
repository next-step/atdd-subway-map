package subway.line.domain;

import subway.section.domain.Section;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public List<Long> getStationIds() {
        return sections.stream().map(
                section -> section.getDownStationId()
        ).collect(Collectors.toList());
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }
}
