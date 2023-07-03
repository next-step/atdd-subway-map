package subway.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class LineUpdateRequest {
    private String name;
    private String color;
}
