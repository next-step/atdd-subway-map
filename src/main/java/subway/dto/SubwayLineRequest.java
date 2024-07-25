package subway.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public final class SubwayLineRequest {
    private final String name;
    private final String color;
    private final Long distance;
    private final Long upStationId;
    private final Long downStationId;
}
