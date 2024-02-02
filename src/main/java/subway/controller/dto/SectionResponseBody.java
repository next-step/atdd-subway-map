package subway.controller.dto;

public class SectionResponseBody {
    private Long id;
    private StationResponseBody upStation;
    private StationResponseBody downStation;
    private Integer distance;

    public SectionResponseBody(Long id, StationResponseBody upStation, StationResponseBody downStation, Integer distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public StationResponseBody getUpStation() {
        return upStation;
    }

    public StationResponseBody getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
