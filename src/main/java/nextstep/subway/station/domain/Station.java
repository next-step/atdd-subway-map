package nextstep.subway.station.domain;

import nextstep.subway.config.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
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

    public boolean isSame(Station otherStation) {
        if (this.equals(otherStation)) return true;
        return this.id.equals(otherStation.id);
    }
}
