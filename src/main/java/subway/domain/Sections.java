package subway.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return List.of();
        }

        List<Station> stations = sections
            .stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());

        stations.add(getLastSection().getDownStation());

        return stations;
    }

    public Section getLastSection() {
        if (sections.isEmpty()) {
            return null;
        }
        return sections.get(sections.size() - 1);
    }

    public void remove(Section section) {
        this.sections.remove(section);
    }

    public boolean isAlreadyExistStation(Station station) {
        return sections.stream().anyMatch(section ->
            station.equals(section.getUpStation()) || station.equals(section.getDownStation())
        );
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

}
