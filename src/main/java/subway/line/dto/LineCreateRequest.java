package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class LineCreateRequest {
    private final String name;
    private final String color;
    private final int upStationId;
    private final int downStationId;
    private final int distance;
}
