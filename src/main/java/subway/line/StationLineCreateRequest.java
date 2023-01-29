package subway.line;

import lombok.Getter;

/**
 * StationLineCreateRequest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@Getter
public class StationLineCreateRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

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
