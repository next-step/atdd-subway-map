package subway.section.dto;

import subway.station.dto.StationResponse;

public class SectionResponse {

    private Long lineId;

    private Long sectionId;

    private StationResponse upStationResponse;

    private StationResponse downStationResponse;

    private Long distance;

    public SectionResponse(Long lineId, Long sectionId, StationResponse upStationResponse, StationResponse downStationResponse, Long distance) {
        this.lineId = lineId;
        this.sectionId = sectionId;
        this.upStationResponse = upStationResponse;
        this.downStationResponse = downStationResponse;
        this.distance = distance;
    }

    public static SectionResponse of(Long lineId, Long sectionId, StationResponse upStationResponse, StationResponse downStationResponse, Long distance) {
        return new SectionResponse(lineId, sectionId, upStationResponse, downStationResponse, distance);

    }

    public Long getLineId() {
        return lineId;
    }
}
