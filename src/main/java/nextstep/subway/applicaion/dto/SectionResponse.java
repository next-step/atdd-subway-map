package nextstep.subway.applicaion.dto;

import java.util.List;

public class SectionResponse {

    private Long lineId;
    private List<StationResponse> stationResponses;

    public SectionResponse(Long lineId, List<StationResponse> stationResponses) {
        this.lineId = lineId;
        this.stationResponses = stationResponses;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
