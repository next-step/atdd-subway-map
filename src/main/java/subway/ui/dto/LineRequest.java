package subway.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;
import subway.domain.Station;

@Getter
@NoArgsConstructor
public class LineRequest {
    private Long id;
    private String name;
    private String color;
    private Station upStation;
    private Station downStation;
    private Integer distance;

    public Line toEntity() {
        return Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
