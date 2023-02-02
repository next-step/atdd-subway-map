package subway.dto;

import lombok.Getter;
import subway.domain.Section;

import java.util.List;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Section> sections;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }
}
