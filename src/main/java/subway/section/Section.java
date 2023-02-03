package subway.section;

import subway.line.Line;
import subway.sectionstation.SectionStation;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<SectionStation> sectionStations = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private Line line;

    private Long distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, long distance, Line line) {
        this.sectionStations.addAll(List.of(new SectionStation(this, upStation), new SectionStation(this, downStation)));
        this.distance = distance;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return sectionStations.get(0).getStation();
    }

    public Station getDownStation() {
        return sectionStations.get(sectionStations.size() - 1).getStation();
    }

    public long getDistance() {
        return distance;
    }

    public void changeLine(Line line) {
        this.line = line;
    }

    public List<Station> getStations() {
        return this.sectionStations.stream()
                .map(SectionStation::getStation)
                .distinct()
                .collect(Collectors.toList());
    }
}
