package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LineUpdateRequest {
    private String name;
    private String color;
}
