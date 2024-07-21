package subway.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import subway.domain.SubwayLine;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public final class SubwayLineRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public SubwayLine toSubwayLine() {
        return new SubwayLine(name, color, upStationId, downStationId, distance);
    }
}
