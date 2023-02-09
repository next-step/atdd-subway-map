package subway.line.dto;

import subway.line.repository.Line;
import subway.section.dto.SectionResponse;
import subway.line.repository.Section;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<SectionResponse> sections = new ArrayList<>();

    private LineResponse(Long id, String name, String color, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;

        sections.forEach(this::addSection);
    }

    public static LineResponse of(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections()
        );
    }

    private void addSection(Section section) {
        sections.add(SectionResponse.of(section));
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

    public List<SectionResponse> getSections() {
        return sections;
    }
}
