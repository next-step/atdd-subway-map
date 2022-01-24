package nextstep.subway.line.domain.dto;

import nextstep.subway.line.domain.model.Distance;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Distance distance;

    private SectionRequest(Long upStationId, Long downStationId, Distance distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public static class Builder {
        private Long upStationId;
        private Long downStationId;
        private Distance distance;

        private Builder() {}

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public SectionRequest build() {
            return new SectionRequest(upStationId, downStationId, distance);
        }
    }
}
