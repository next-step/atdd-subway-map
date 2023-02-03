package subway.station;

import subway.section.Section;
import subway.sectionstation.SectionStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "station")
    private List<SectionStation> sectionStation = new ArrayList<>();

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

    public void addSection(Section section) {
        this.sectionStation.add(new SectionStation(section, this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(sectionStation, station.sectionStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sectionStation);
    }
}
