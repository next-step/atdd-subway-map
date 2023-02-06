package subway.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LineRequestDto {
    private final String lineName;
    private final String lineColor;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    @Builder
    public LineRequestDto(String lineName, String lineColor, Long upStationId, Long downStationId, Long distance) {
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
