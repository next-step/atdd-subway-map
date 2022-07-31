package nextstep.subway.domain;

import javax.persistence.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

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

    public void setLine(Line line) {
        this.line = line;
    }

    public void removeLine() {
        this.line = null;
    }
}
