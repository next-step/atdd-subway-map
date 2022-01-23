package nextstep.subway.domain;


import java.util.Objects;

public class SectionRequest {

    private final long upStationId;
    private final long downStationId;
    private final int distance;

    private SectionRequest(long upStationId, long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionRequest of(long upStationId, long downStationId, int distance) {
        return new SectionRequest(upStationId, downStationId, distance);
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionRequest that = (SectionRequest) o;
        return upStationId == that.upStationId && downStationId == that.downStationId && distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId, distance);
    }
}
