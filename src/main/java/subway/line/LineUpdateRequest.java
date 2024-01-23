package subway.line;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LineUpdateRequest {
    private String name;
    private String color;
}
