package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "station", fetch = LAZY)
    private final List<Section> lines = new ArrayList<>();

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

    public void updateSubwayLine(Section section) {
        this.lines.add(section);
    }

    public void removeSubwayLine(Section section) {
        this.lines.remove(section);
    }
}
