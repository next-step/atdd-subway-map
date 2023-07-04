package subway.line.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineCreateRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;
}
