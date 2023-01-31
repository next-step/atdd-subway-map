package subway.presentation.line.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LineUpdateRequest {
    private String name;
    private String color;
}