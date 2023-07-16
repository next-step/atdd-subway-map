package subway.dto;

public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;

    private SectionRequest() {

    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long downStationId;
        private Long upStationId;
        private Long distance;

        private Builder() {

        }

        public Builder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public Builder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public SectionRequest build() {
            SectionRequest sectionRequest = new SectionRequest();
            sectionRequest.upStationId = this.upStationId;
            sectionRequest.downStationId = this.downStationId;
            sectionRequest.distance = this.distance;
            return sectionRequest;
        }
    }

}
