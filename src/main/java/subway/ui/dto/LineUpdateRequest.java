package subway.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LineUpdateRequest {
    private String name;
    private String color;
}
