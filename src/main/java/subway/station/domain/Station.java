package subway.station.domain;

import javax.persistence.*;
import java.util.Objects;

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


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !getClass().isAssignableFrom(obj.getClass()))
            return false;

        Station station = (Station) obj;
        return Objects.equals(id, station.getId()) && Objects.equals(name, station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
