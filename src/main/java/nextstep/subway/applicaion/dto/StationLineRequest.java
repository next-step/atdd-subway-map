package nextstep.subway.applicaion.dto;

import lombok.*;
import nextstep.subway.domain.StationLine;

@Getter
@NoArgsConstructor
public class StationLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    @Builder
    public StationLineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationLine toEntity() {
        return StationLine.builder()
            .name(name)
            .color(color)
            .upStationId(upStationId)
            .downStationId(downStationId)
            .distance(distance)
            .build();
    }

}
