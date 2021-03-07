package nextstep.subway.line.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionRequest(Builder builder) {
        this.upStationId = builder.upStationId;
        this.downStationId = builder.downStationId;
        this.distance = builder.distance;
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

    public static Builder Builder() {
        return new Builder();
    }

    public static class Builder{
        private Long upStationId;
        private Long downStationId;
        private int distance;

        public Builder upStationId(Long val) {
            this.upStationId = val;
            return this;
        }
        public Builder downStationId(Long val) {
            this.downStationId = val;
            return this;
        }
        public Builder distance(int val) {
            this.distance = val;
            return this;
        }

        public SectionRequest build() {
            return new SectionRequest(this);
        }

    }
}
