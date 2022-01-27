package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

import java.time.LocalDateTime;

public class SectionSaveResponse {

    private final String downStationId;
    private final String upStationId;
    private final int distance;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public SectionSaveResponse(final Section section) {
        this(section.getDownStation().getName(), section.getUpStation().getName(),
                section.getDistance(), section.getCreatedDate(), section.getModifiedDate());
    }

    public SectionSaveResponse(final String downStationId, final String upStationId,
                               final int distance, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
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
