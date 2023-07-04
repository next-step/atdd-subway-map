package subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public void addSection(Line line, Station upStation, Station downStation, Integer distance) {
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        sections.forEach(section -> stations.add(section.getUpStation()));
        addLastStation(stations);
        return stations;
    }

    private void addLastStation(List<Station> stations) {
        if (sections.size() > 0) {
            Section lastSection = sections.get(sections.size() - 1);
            stations.add(lastSection.getDownStation());
        }
    }
}
