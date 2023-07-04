package subway.line.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineModifyRequest {

    private String name;

    private String color;
}
