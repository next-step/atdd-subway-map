package subway.route.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Stations stations;

    @Column(length = 10, nullable = false)
    private Integer distance;

    public Route() {
    }

    public Route(Long id, String name, String color, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public static Route of(Long id, String name, String color, Integer distance) {
        return new Route(id, name, color, distance);
    }

    public void saveStations(Stations stations) {
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Stations getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (!Objects.equals(id, route.id)) return false;
        return Objects.equals(name, route.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
