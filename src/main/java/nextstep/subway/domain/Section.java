package nextstep.subway.domain;

public class Section {

    private Long upStationId;
    private Long downStationId;
    private int distance;

    protected Section() {
    }

    public Section(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public boolean hasDownStationId(long downStationId) {
        return this.downStationId.equals(downStationId);
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
