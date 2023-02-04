package subway.section.domain;

import lombok.Getter;
import subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Embeddable
@Getter
public class Sections {

    @OneToMany(mappedBy = "line", cascade = ALL, orphanRemoval = true)
    private final List<Section> sectionList = new ArrayList<>();

    public void addSection(final Section sections) {
        sectionList.add(sections);
    }

    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();

        sectionList.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return stations;
    }

    public boolean isNotExistDownStation(Station station) {
        return sectionList.stream()
                .map(Section::getDownStation)
                .noneMatch(downStation -> downStation.equals(station));
    }

    public boolean isAlreadyRegisteredStation(final Station station) {
        return sectionList.stream()
                .anyMatch(section -> section.getUpStation().equals(station)
                        || section.getDownStation().equals(station));
    }
}
