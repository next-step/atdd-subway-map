package subway.controller.resonse;

import subway.domain.Section;

public class SubwayLineSectionResponse {

    private long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private long distance;

    private SubwayLineSectionResponse() {
    }

    private SubwayLineSectionResponse(long id, StationResponse upStation, StationResponse downStation, long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SubwayLineSectionResponse of(Section newSection) {
        StationResponse upStationResponse = new StationResponse(newSection.getUpStation());
        StationResponse downStationResponse = new StationResponse(newSection.getDownStation());
        return new SubwayLineSectionResponse(newSection.getId(), upStationResponse, downStationResponse, newSection.getDistance());
    }

    public long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public long getDistance() {
        return distance;
    }
}
