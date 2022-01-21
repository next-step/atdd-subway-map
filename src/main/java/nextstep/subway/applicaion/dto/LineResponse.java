package nextstep.subway.applicaion.dto;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import nextstep.subway.domain.Line;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

	public static List<LineResponse> fromList(List<Line> lines) {
        return lines.stream().map(LineResponse::from).collect(toList());
	}

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getCreatedDate(), line.getModifiedDate());
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
