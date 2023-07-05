package subway.station;

import subway.line.Line;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "LINE_ID")
    private Line line;

    public Station() {

    }

    public Station(String name) {
        this.name = name;
    }
    public Station(Long id, Line line) {
        this.id = id;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }
}
