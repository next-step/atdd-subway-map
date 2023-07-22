package subway.controller.dto;

import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {

    private final Long id;

    private final String name;

    private final String color;

    private final StationResponse upStation;

    private final StationResponse downStation;

    private final Long distance;

    public LineResponse(Long id, String name, String color, Station upStation, Station downStation, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = StationResponse.responseFrom(upStation);
        this.downStation = StationResponse.responseFrom(downStation);
        this.distance = distance;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId()
            , line.getName()
            , line.getColor()
            , line.getEndStations().upEndStation().getStation()
            , line.getEndStations().downEndStation().getStation()
            , line.getDistance());
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

    public Long getDistance() {
        return distance;
    }
}
