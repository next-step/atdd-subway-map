package subway.dto;

import lombok.Getter;

@Getter
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
