package subway.line.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LineUpdateRequest {

    private Long lineId;
    private String name;
    private String color;

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static LineUpdateRequest of(String name, String color) {
        return new LineUpdateRequest(name, color);
    }
}
