package subway.line.section;

import subway.station.StationResponse;

import java.util.List;

public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;

    public SectionResponse(Long id, StationResponse upStation, StationResponse downStation, Long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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
    //    private Long stationId;
//    private String name;
//    private Long distanceFromPrev;
//
//    public SectionResponse(Long stationId, String name, Long distanceFromPrev) {
//        this.stationId = stationId;
//        this.name = name;
//        this.distanceFromPrev = distanceFromPrev;
//    }
//
//    public Long getStationId() {
//        return stationId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Long getDistanceFromPrev() {
//        return distanceFromPrev;
//    }
}
