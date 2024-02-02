package subway.line;

import subway.line.section.Section;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<Section> sections;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.sections = line.getSections();
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

    public List<Section> getSections() {
        return sections;
    }
}
