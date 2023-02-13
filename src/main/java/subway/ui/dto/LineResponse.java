package subway.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

@Getter
@NoArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public LineResponse(Line entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
        this.upStationId = entity.getUpStationId();
        this.downStationId = entity.getDownStationId();
        this.distance = entity.getDistance();
    }
}
