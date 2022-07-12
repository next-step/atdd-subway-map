package nextstep.subway.section.application.dto;

public class SectionResponse {
    
    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public SectionResponse() {
        this.id = null;
        this.upStationId = null;
        this.downStationId = null;
        this.distance = null;
    }

    public SectionResponse(final Long id, final Long upStationId, final Long downStationId, final Long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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

    public static class SectionResponseBuilder {
        private Long id;
        private Long upStationId;
        private Long downStationId;
        private Long distance;

        public SectionResponse.SectionResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public SectionResponse.SectionResponseBuilder upStationId(final Long upStationId) {
            this.upStationId = upStationId;
            return this;

        }

        public SectionResponse.SectionResponseBuilder downStationId(final Long downStationId) {
            this.downStationId = downStationId;
            return this;

        }

        public SectionResponse.SectionResponseBuilder distance(final Long distance) {
            this.distance = distance;
            return this;
        }

        public SectionResponse build() {
            return new SectionResponse(id, upStationId, downStationId, distance);
        }

    }

    public static SectionResponse.SectionResponseBuilder builder() {
        return new SectionResponse.SectionResponseBuilder();
    }

}
