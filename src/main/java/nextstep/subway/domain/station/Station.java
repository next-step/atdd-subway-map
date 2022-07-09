package nextstep.subway.domain.station;

import nextstep.subway.domain.subwayLine.SubwayLine;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "subway_line_id")
    private SubwayLine subwayLine;

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

    public void updateSubwayLine(SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }
}
