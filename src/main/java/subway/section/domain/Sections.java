package subway.section.domain;

import lombok.Getter;
import subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Embeddable
@Getter
public class Sections {

    @OneToMany(mappedBy = "line", orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void addSection(final Section section) {
        this.sections.add(section);
    }

    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();

        sections.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return stations;
    }
}
