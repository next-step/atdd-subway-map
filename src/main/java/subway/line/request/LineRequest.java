package subway.line.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineRequest {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

}