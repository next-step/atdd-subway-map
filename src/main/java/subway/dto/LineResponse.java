package subway.dto;

import lombok.Getter;
import subway.domain.Sections;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Sections sections;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }
}
