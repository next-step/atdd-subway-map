package nextstep.subway.line.dto;

public class SectionRequest {
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    private SectionRequest(
        long upStationId,
        long downStationId,
        int distance
    ) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public static SectionRequest of(long upStationId, long downStationId, int distance) {
        return new SectionRequest(upStationId, downStationId, distance);
    }
}
