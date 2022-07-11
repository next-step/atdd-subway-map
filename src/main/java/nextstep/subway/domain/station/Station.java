package nextstep.subway.domain.station;

import nextstep.subway.domain.m2m.StationToSubwayLine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "station", fetch = LAZY)
    private final List<StationToSubwayLine> subwayLines = new ArrayList<>();

    protected Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateSubwayLine(StationToSubwayLine stationToSubwayLine) {
        this.subwayLines.add(stationToSubwayLine);
    }

    public void removeSubwayLine(StationToSubwayLine stationToSubwayLine) {
        this.subwayLines.remove(stationToSubwayLine);
    }
}
