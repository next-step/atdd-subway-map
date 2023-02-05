package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Sections;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Sections sections;
}
