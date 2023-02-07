package subway.line;

import subway.station.Station;

import java.util.ArrayList;

public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private ArrayList<Station> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        // station select 코드
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

    public ArrayList<Station> getStations() {
        return stations;
    }
}
