package nextstep.subway.domain;

public class Section {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Section(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section() {
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

    public void setUpStationId(final Long upStationId) {
        this.upStationId = upStationId;
    }

    public void setDownStationId(final Long downStationId) {
        this.downStationId = downStationId;
    }

    public void setDistance(final int distance) {
        this.distance = distance;
    }
}
