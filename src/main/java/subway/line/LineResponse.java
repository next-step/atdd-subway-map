package subway.line;

import java.util.List;
import java.util.Objects;
import subway.station.Station;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public List<Station> getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineResponse that = (LineResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(),
            that.getName()) && Objects.equals(getColor(), that.getColor())
            && Objects.equals(getStations(), that.getStations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getStations());
    }
}
