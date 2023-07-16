package subway.section;

import subway.station.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line")
    private List<Section> sections;

    public Sections() {}

    public boolean contains(Station station) {
        return sections.stream().anyMatch(section -> section.contains(station));
    }

    public Optional<Section> findByDownStationId(Long downStationId) {
        return sections.stream()
                .filter(section -> section.getDownStationId().equals(downStationId))
                .findFirst();
    }

    public void delete(Section section) {
        sections.remove(section);
    }
}
