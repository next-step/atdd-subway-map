package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    private String name;

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

    public void line(Line line) {
        this.line = line;
    }
}
