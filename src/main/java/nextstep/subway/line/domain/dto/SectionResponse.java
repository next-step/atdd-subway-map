package nextstep.subway.line.domain.dto;

import java.time.LocalDateTime;

import nextstep.subway.line.domain.model.Section;

public class SectionResponse {
    private final Long id;
    private final String upStationName;
    private final String downStationName;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private SectionResponse(Long id, String upStationName, String downStationName, LocalDateTime createdDate,
                           LocalDateTime modifiedDate) {
        this.id = id;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
            .id(section.getId())
            .upStationName(section.getUpStation().getName())
            .downStationName(section.getDownStation().getName())
            .modifiedDate(section.getModifiedDate())
            .createdDate(section.getCreatedDate())
            .build();
    }


    public Long getId() {
        return id;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public static class Builder {
        private Long id;
        private String upStationName;
        private String downStationName;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder upStationName(String upStationName) {
            this.upStationName = upStationName;
            return this;
        }

        public Builder downStationName(String downStationName) {
            this.downStationName = downStationName;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public SectionResponse build() {
            return new SectionResponse(id, upStationName, downStationName, createdDate, modifiedDate);
        }
    }
}
