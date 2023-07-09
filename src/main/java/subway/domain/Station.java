package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATION_ID")
    private Long id;

    @Column(name = "NAME", length = 20, nullable = false)
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
}
