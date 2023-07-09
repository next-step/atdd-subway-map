package subway.section.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import subway.line.domain.Line;
import subway.station.domain.Station;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @Embedded
    private SectionStations stations;

    private Integer distance;

    protected Section() {}

    public Section(Line line, SectionStations stations, Integer distance) {
        this.line = line;
        this.stations = stations;
        this.distance = distance;
    }

    public Station getUpwardStation() {
        return stations.getUpStation();
    }

    public SectionStations getStations() {
        return stations;
    }
}
