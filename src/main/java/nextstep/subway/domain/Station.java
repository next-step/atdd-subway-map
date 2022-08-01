package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Station {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<SectionStation> sectionStations;

    public void update(Station station) {
        if (station == null) {
            return;
        }

        this.name = station.getName() != null ? station.getName() : this.name;
    }

    public Station() {
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

    public void addSectionStation(SectionStation sectionStation) {
        this.sectionStations.add(sectionStation);
    }
}
