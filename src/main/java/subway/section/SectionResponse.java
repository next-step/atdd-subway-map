package subway.section;

import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class SectionResponse {
    private Long lineId;
    private String lineName;

    private List<StationResponse> upStations = new ArrayList<>();
    private List<StationResponse> downStations = new ArrayList<>();
    public SectionResponse() {
    }

    public SectionResponse(Long lineId, String lineName) {
        this.lineId = lineId;
        this.lineName = lineName;
    }

    public void addUpStation(StationResponse response) {
        upStations.add(response);
    }

    public void addDownStation(StationResponse response) {
        downStations.add(response);
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationResponse> getUpStations() {
        return upStations;
    }

    public List<StationResponse> getDownStations() {
        return downStations;
    }
}
