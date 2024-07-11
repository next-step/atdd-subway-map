package subway.line.application.dto;

import subway.line.domain.Section;

public class SectionResponse {
    private Long sectionId;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionResponse(Long sectionId, Long upStationId, Long downStationId, Integer distance) {
        this.sectionId = sectionId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponseBuilder builder() {
        return new SectionResponseBuilder();
    }

    public Long getSectionId() {
        return sectionId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponseBuilder()
            .sectionId(section.getId())
            .upStationId(section.getUpStationId())
            .downStationId(section.getDownStationId())
            .distance(section.getDistance())
            .build();
    }


    public static class SectionResponseBuilder {
        private Long sectionId;
        private Long upStationId;
        private Long downStationId;
        private Integer distance;

        SectionResponseBuilder() {
        }

        public SectionResponseBuilder sectionId(Long sectionId) {
            this.sectionId = sectionId;
            return this;
        }

        public SectionResponseBuilder upStationId(Long upStationId) {
            this.upStationId = upStationId;
            return this;
        }

        public SectionResponseBuilder downStationId(Long downStationId) {
            this.downStationId = downStationId;
            return this;
        }

        public SectionResponseBuilder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public SectionResponse build() {
            return new SectionResponse(this.sectionId, this.upStationId, this.downStationId, this.distance);
        }
    }
}
