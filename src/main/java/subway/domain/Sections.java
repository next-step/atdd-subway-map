package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean hasLastStation(Station upStation) {
        List<Station> stations = getStations();
        return stations.get(stations.size() - 1).equals(upStation);
    }

    public boolean hasStation(Station station) {
        return getStations().contains(station);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());
        stations.add(0, sections.get(0).getUpStation());
        return stations;
    }

    public Station getLastStation() {
        List<Station> stations = getStations();
        return stations.get(stations.size() - 1);
    }

    public boolean hasSingleSection() {
        return sections.size() == 1;
    }

    public void remove() {
        sections.remove(sections.size() - 1);
    }
}
