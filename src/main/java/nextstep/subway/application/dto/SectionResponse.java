package nextstep.subway.application.dto;

import java.time.LocalDateTime;

public class SectionResponse {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SectionResponse(Long id, Long upStationId, Long downStationId, int distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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
