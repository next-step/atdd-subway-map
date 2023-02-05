package subway.dto.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import subway.domain.line.Line;

@AllArgsConstructor
public class UpdateLineRequest {
    private String name;
    private String color;

    public Line convertToEntity() {
        return new Line(name, color);
    }

}
