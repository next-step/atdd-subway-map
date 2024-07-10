package subway;

import java.util.ArrayList;
import java.util.List;

public class SectionResponse {
    private Long lineId;
    private String lineName;

    private List<StationResponse> stations = new ArrayList<>();

    public SectionResponse(Long lineId, String lineName) {
        this.lineId = lineId;
        this.lineName = lineName;
    }

    public void addStation(StationResponse response) {
        stations.add(response);
    }

    public SectionResponse() {
    }

    public SectionResponse(Long lineId, String lineName, List<StationResponse> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
