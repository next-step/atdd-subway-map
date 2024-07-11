package subway.dto;

import subway.domain.SubwayLine;

public final class SubwayLineRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public SubwayLineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SubwayLine toSubwayLine() {
        return new SubwayLine(name, color, upStationId, downStationId, distance);
    }
}
