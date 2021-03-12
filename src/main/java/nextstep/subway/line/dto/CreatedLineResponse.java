package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;


public class CreatedLineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private CreatedLineResponse() {
    }

    private CreatedLineResponse(Long id,
                        String name,
                        String color,
                        Long upStationId,
                        Long downStationId,
                        int distance,
                        LocalDateTime createdDate,
                        LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static CreatedLineResponse of(Line line) {
        return new CreatedLineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                line.getLastUpStation().getId(),
                line.getLastDownStation().getId(),
                line.getLineDistance(),
                line.getCreatedDate(),
                line.getModifiedDate());
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

    public Long getUpStationId() { return upStationId; }

    public Long getDownStationId() { return downStationId; }

    public int getDistance() { return distance; }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
