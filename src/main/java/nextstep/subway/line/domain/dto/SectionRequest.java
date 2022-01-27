package nextstep.subway.line.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import nextstep.subway.line.domain.model.Distance;

public class SectionRequest {
    private long upStationId;
    private long downStationId;
    private Distance distance;

    private SectionRequest() {
    }

    private SectionRequest(long upStationId, long downStationId, Distance distance) {
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

        public Builder upStationId(long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(long downStationId) {
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
