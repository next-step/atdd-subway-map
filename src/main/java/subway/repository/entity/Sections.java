package subway.repository.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Section> sections = new LinkedList<>();

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        var stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(getLastDownStation());

        return stations;
    }

    private Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public List<Long> getStationIds() {
        return getStations()
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    private boolean isStationExist(Station station) {
        return getStations().stream()
                .map(Station::getId)
                .anyMatch(stationId -> stationId.equals(station.getId()));
    }

}
