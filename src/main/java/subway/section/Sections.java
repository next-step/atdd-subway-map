package subway.section;

import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void remove() {
        sections.remove(getLastStationIndex());
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stations.add(0, sections.get(0).getUpStation());
        return stations;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isLastStation(Station station) {
        return getLastStation().equals(station);
    }

    public Station getLastStation() {
        return sections.get(getLastStationIndex()).getDownStation();
    }

    public boolean containsStation(Station station) {
        return getStations().contains(station);
    }

    public boolean isSingleSection() {
        return sections.size() == 1;
    }

    private int getLastStationIndex() {
        return sections.size() - 1;
    }

    public int calcDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }
}
