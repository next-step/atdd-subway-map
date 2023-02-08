package subway.dto.line;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateLineRequest {
    private String name;
    private String color;

}
