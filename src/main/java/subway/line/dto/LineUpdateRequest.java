package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineUpdateRequest {

    private Long id;
    private String name;
    private String color;

    public static LineUpdateRequest of(Long id, String name, String color) {
        return new LineUpdateRequest(id, name, color);
    }
}
