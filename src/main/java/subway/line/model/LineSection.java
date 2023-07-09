package subway.line.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import subway.station.model.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
//@AllArgsConstructor
@NoArgsConstructor
public class LineSection {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        this.sections.add(section);
    }

    public List<Station> getDownStations() {
        List<Station> stations = new ArrayList<>();
        for (Section section : this.sections) {
            stations.add(section.getDownStation());
        }
        return stations;
    }

    public int getStationsCount() {
        return sections.size();
    }

    public Section get(int index) {
        return sections.get(index);
    }

    public void remove(Section section) {
        sections.remove(section);
    }
}
