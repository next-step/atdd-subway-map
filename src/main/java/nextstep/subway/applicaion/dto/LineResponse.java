package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.station.Station;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;

    public LineResponse(Long id, String name, String color, Station upStation, Station downStation, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = new StationResponse(upStation.getId(), upStation.getName());
        this.downStation = new StationResponse(downStation.getId(), downStation.getName());
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }
    public String getColor() {
        return color;
    }

    public Long getDistance() {
        return distance;
    }
}
