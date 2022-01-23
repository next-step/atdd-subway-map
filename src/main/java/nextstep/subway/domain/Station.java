package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationRequest;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
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

    public Station(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public static Station of(String name) {
        return new Station(null, name);
    }

    public static Station of(long id) {
        return new Station(id, null);
    }

    public static Station of(long id, String name) {
        return new Station(id, name);
    }

    public static Station of(StationRequest stationRequest) {
        return new Station(null, stationRequest.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
