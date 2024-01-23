package subway;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = true)
    private Long lineId;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, Long lineId) {
        this.name = name;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
