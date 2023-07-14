package subway.route.dto;

import subway.route.domain.Route;
import subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class RouteResponse {

    private final Long id;

    private final String name;
    private final String color;
    private final List<Station> stations;

    public RouteResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static RouteResponse of(Route route) {
        return new RouteResponse(route);
    }

    private RouteResponse(Route route) {
        this.id = route.getId();
        this.name = route.getName();
        this.color = route.getColor();
        stations = List.of(route.getStations().getUpStationId(), route.getStations().getDownStationId());
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

    public List<Station>  getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteResponse that = (RouteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations);
    }

    @Override
    public String toString() {
        return "RouteResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stations=" + stations +
                '}';
    }
}
