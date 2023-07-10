package subway.line.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyLineRequest {
    private String name;
    private String color;

    public LineDto toDto() {
        return new LineDto(
                name,
                color
        );
    }
}
