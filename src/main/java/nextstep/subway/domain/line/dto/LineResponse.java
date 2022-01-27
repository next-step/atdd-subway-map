package nextstep.subway.domain.line.dto;

import nextstep.subway.domain.line.Line;

import java.time.LocalDateTime;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private LineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse from(Line savedLine) {
        return new LineResponse(savedLine.getId(), savedLine.getName(), savedLine.getColor(),
                savedLine.getCreatedDate(), savedLine.getModifiedDate());
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
