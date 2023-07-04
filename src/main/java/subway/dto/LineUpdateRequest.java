package subway.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineUpdateRequest {
    private String name;
    private String color;
}
