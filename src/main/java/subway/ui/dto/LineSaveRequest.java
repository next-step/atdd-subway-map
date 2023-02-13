package subway.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

@Getter
@NoArgsConstructor
public class LineSaveRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Line toEntity() {
        return Line.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
