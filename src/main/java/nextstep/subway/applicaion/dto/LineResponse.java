package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line.Line;

@Getter
@NoArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    @Builder
    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.upStationId = line.upStation().getId();
        this.downStationId = line.downStation().getId();
        this.distance = line.distance();
    }

}
