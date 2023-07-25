package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineModifyRequest {
    private String name;
    private String color;
}
