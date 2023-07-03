package subway.dto;

import lombok.*;
import subway.domain.Line;
import subway.domain.Station;

@Getter
@AllArgsConstructor
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public Line toEntity(Station upStation, Station downStation){
        return new Line(name, color, upStation, downStation, distance);
    }
}
