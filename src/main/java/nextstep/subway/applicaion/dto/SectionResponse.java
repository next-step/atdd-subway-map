package nextstep.subway.applicaion.dto;

import java.util.List;

public class SectionResponse {

    private Long lineId;
    private List<StationResponse> stations;

    public SectionResponse(Long lineId, List<StationResponse> stations) {
        this.lineId = lineId;
        this.stations = stations;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
