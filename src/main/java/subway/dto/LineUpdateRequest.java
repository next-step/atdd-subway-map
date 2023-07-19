package subway.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LineUpdateRequest {
    private String name;
    private String color;
}
