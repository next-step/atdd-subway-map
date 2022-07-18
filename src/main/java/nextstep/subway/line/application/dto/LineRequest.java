package nextstep.subway.line.application.dto;

import lombok.Getter;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class LineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Min(1)
    private long upStationId;
    @Min(1)
    private long downStationId;
    @Min(0)
    private int distance;

    public Line toEntity(Section firstSection) {
        return new Line(name, color, firstSection);
    }
}
