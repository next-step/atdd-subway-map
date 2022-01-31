package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<SectionResponse> sectionsResponse;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private LineResponse(Long id, String name, String color, List<SectionResponse> sectionsResponse,
                         LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sectionsResponse = sectionsResponse;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), sectionsResponseMapper(line.getSections()),
                line.getCreatedDate(), line.getModifiedDate());
    }

    private static List<SectionResponse> sectionsResponseMapper(Sections sections) {
        return sections.getSections().stream().map(section -> new SectionResponse(section)).collect(Collectors.toList());
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
