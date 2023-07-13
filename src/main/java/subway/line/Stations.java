package subway.line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.line.section.Section;
import subway.station.Station;

@Embeddable
public class Stations {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public Stations() {
    }

    public Stations(List<Section> sections) {
        this.sections = sections;
    }

    public Stations(Line line, Station upStationId, Station downStationId) {
        this(List.of(new Section(line, upStationId, downStationId, 1)));
    }

    public List<Station> getStations() {
        return toStations();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    private List<Station> toStations() {
        return Stream.concat(Stream.of(this.sections.get(0).getUpStation()),
                this.sections.stream().map(Section::getDownStation))
            .collect(Collectors.toList());
    }
}
