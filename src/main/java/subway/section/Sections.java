package subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.station.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "lineId", cascade = CascadeType.PERSIST)
    private List<Section> sections = new ArrayList<>();

    public void addSections(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        var upStations = sections.stream().map(Section::getUpStation);
        var downStations = sections.stream().map(Section::getDownStation);

        return Stream.concat(upStations, downStations).distinct().collect(Collectors.toList());
    }
}
