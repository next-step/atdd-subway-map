package subway.domain;

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

    protected Station() {
    }

    public Station(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setLine(final Line line) {
        this.line = line;
    }
}
