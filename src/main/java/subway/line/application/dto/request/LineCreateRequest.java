package subway.line.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LineCreateRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    @Builder
    private LineCreateRequest(final String name, final String color,
                             final Long upStationId, final Long downStationId, final Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
