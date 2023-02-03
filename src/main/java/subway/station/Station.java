package subway.station;

import subway.line.Line;
import subway.section.Section;
import subway.sectionstation.SectionStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}
