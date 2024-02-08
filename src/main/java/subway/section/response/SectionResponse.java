package subway.section.response;

import lombok.Builder;
import lombok.Getter;
import subway.station.response.StationResponse;

@Getter
public class SectionResponse {
    private Long id;
    private Long lineId;
    private StationResponse upStations;
    private StationResponse downStations;
    private Long distance;

    @Builder

    public SectionResponse(Long id, Long lineId, StationResponse upStations, StationResponse downStations, Long distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStations = upStations;
        this.downStations = downStations;
        this.distance = distance;
    }
}
