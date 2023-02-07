package subway.dto;

public class SectionResponse {
    private Long id;
    private Long distance;
    StationResponse downStation;
    StationResponse upStation;

    public SectionResponse(Long id, Long distance, StationResponse downStationResponse, StationResponse upStationResponse) {
        this.id = id;
        this.distance = distance;
        this.downStation = downStationResponse;
        this.upStation = upStationResponse;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public StationResponse getUpStation() {
        return upStation;
    }
}
