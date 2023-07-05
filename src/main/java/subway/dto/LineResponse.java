package subway.dto;

import subway.model.Station;

import java.util.List;

public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<Station> stations;

    public LineResponse(Long id, String name, String color, Station upStation, Station downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = List.of(upStation, downStation);
    }

    public Long getId() {
        return id;
    }
}
