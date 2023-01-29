package subway.domain;

import java.util.List;

public class LineDomain {

    private Long id;
    private String name;
    private String color;
    private List<StationDomain> stations;

    public LineDomain(Long id, String name, String color, List<StationDomain> stations) {
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

    public List<StationDomain> getStations() {
        return stations;
    }

}
