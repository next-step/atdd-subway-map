package nextstep.subway.section.application.dto;

public class SectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public SectionRequest() {
        this.upStationId = null;
        this.downStationId = null;
        this.distance = null;
    }

    public SectionRequest(final Long upStationId, final Long downStationId, final Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public static class SectionRequestBuilder {
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public SectionRequest.SectionRequestBuilder upStationId(final Long upStationId) {
            this.upStationId = upStationId;
            return this;

        }

        public SectionRequest.SectionRequestBuilder downStationId(final Long downStationId) {
            this.downStationId = downStationId;
            return this;

        }

        public SectionRequest.SectionRequestBuilder distance(final Long distance) {
            this.distance = distance;
            return this;
        }

        public SectionRequest build() {
            return new SectionRequest(upStationId, downStationId, distance);
        }

    }

    public static SectionRequest.SectionRequestBuilder builder() {
        return new SectionRequest.SectionRequestBuilder();
    }

}
