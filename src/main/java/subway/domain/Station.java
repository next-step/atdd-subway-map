package subway.domain;

import javax.persistence.*;
import java.util.List;
import java.util.stream.DoubleStream;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
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

    public boolean containsAny(List<Long> stationIds) {
        return stationIds.contains(this.id);
    }
}
