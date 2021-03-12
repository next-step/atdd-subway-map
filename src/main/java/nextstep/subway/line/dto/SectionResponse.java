package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.time.LocalDateTime;

public class SectionResponse {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SectionResponse() {
    }

    public SectionResponse(Long id,
                           Long upStationId,
                           Long downStationId,
                           int distance,
                           LocalDateTime createdDate,
                           LocalDateTime modifiedDate) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate());
    }

    public Long getId() {
        return this.id;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
