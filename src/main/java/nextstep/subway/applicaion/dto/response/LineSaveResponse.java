package nextstep.subway.applicaion.dto.response;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;

import java.time.LocalDateTime;

public class LineSaveResponse {
    private Long id;
    private String name;
    private String color;
    private Section section;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineSaveResponse(Long id, String name, String color, Section section,
                            LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.section = section;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineSaveResponse createLineResponse(Line line, Section section) {
        return new LineSaveResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                section,
                line.getCreatedDate(),
                line.getModifiedDate()
        );
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

    public Section getSection() {
        return section;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
