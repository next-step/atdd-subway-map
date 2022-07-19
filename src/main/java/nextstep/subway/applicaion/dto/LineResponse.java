package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    List<Station> stations;

    public LineResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
