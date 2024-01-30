package subway.line;

import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sectionList = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public static Sections from(List<Section> sectionList) {
        return new Sections(sectionList);
    }

    public List<Station> startStations() {
        return this.sectionList.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    public Station lastStation() {
        return this.sectionList.get(sectionList.size() - 1).getDownStation();
    }
}
