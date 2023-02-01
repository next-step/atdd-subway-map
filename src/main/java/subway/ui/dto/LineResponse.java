package subway.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;
import subway.domain.Station;

@Getter
@NoArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Station upStation;
    private Station downStation;
    private Integer distance;

    public LineResponse(Line entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
        this.upStation = entity.getUpStation();
        this.downStation = entity.getDownStation();
        this.distance = entity.getDistance();
    }
}
