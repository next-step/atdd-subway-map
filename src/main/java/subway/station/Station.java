package subway.station;

import javax.persistence.*;
import subway.line.Line;

@Entity
@Table(name = "STATION")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LINE_ID")
    private Line line;

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

    public void changeLine(Line line) {
        if (this.line != null) {
            this.line.removeStation(this);
        }

        this.line = line;
    }
}
