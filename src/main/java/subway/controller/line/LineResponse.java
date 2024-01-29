package subway.controller.line;

import subway.controller.station.StationResponse;
import subway.service.line.Line;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final StationResponse upStation;
    private final StationResponse downStation;

    private LineResponse(Long id, String name, String color, StationResponse upStation, StationResponse downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static LineResponse from(Line line) {
        StationResponse upStation = new StationResponse(line.getUpStation().getId(), line.getUpStation().getName());
        StationResponse downStation = new StationResponse(line.getDownStation().getId(), line.getDownStation().getName());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), upStation, downStation);
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

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }
}
