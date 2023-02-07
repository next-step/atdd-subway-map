package subway.dto;

public class SectionResponse {
    private Long id;
    StationResponse downStation;
    StationResponse upStation;

    public SectionResponse(Long id, StationResponse downStationResponse, StationResponse upStationResponse) {
        this.id = id;
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
