package subway.section;

import subway.station.Station;

import javax.persistence.*;
import java.util.Set;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line")
    private Set<Section> sections;

    public Sections() {}

    public boolean contains(Station station) {
        return sections.stream().anyMatch(section -> section.contains(station));
    }
}
