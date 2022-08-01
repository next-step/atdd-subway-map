package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public List<Section> value() {
        return sections;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public Section get(int index) {
        return this.sections.get(index);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (Section section : this.sections) {
            stations.addAll(
                    section.getStations()
                        .stream()
                        .filter(station -> !stations.contains(station))
                        .collect(Collectors.toList()));
        }
        return stations;
    }
}
