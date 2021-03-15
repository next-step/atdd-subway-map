package nextstep.subway.section.dto;

import nextstep.subway.section.domain.Section;

import java.time.LocalDateTime;

public class SectionResponse {

    private Long id;
    private Long downStationId;
    private Long upStationId;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SectionResponse() {
    }

    public SectionResponse(Long id, Long downStationId, Long upStationId, int distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static SectionResponse of(Section section){
        return new SectionResponse(section.getId(), section.getDownStation().getId(), section.getUpStation().getId(), section.getDistance(), section.getCreatedDate(), section.getModifiedDate());
    }

    public Long getId() {
        return id;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
