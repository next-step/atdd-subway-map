package subway.line;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LineModifyRequest {
    private String name;

    private String color;
}
