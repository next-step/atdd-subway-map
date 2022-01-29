package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;

public class SectionResponse {
    private Long id;
    private Long lineId;
    private Long downStationId;
    private Long upStationId;
    private Integer distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SectionResponse() {
    }

    public SectionResponse(Long id, Long lineId, Long downStationId, Long upStationId, Integer distance, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.lineId = lineId;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
