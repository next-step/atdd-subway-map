package nextstep.subway.linestation.dto;

import nextstep.subway.linestation.domain.LineStation;
import nextstep.subway.station.domain.Station;

public class LineStationRequest {
    private Long preStationId;
    private Long stationId;
    private String distance;
    private String duration;

    public LineStationRequest() {
    }

    public LineStationRequest(Long preStationId, Long stationId, String distance, String duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public void setPreStationId(Long preStationId) {
        this.preStationId = preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "LineStationRequest{" +
                "preStationId='" + preStationId + '\'' +
                ", stationId='" + stationId + '\'' +
                ", distance='" + distance + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public LineStation toLineStation(Station station, Station preStation) {
        return new LineStation(station, preStation, Integer.parseInt(duration), Integer.parseInt(distance));
    }
}
