package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalDateTime;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public static LineResponse of(final Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getUpStationId(),
                line.getDownStationId(),
                line.getDistance(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private LineResponse(
            final Long id, final String name, final String color,
            final Long upStationId, final Long downStationId, final int distance,
            final LocalDateTime createdDate, final LocalDateTime modifiedDate
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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
