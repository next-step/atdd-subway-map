package subway.line;

import subway.section.SubwaySection;
import subway.station.Station;

import java.util.List;

public class SubwayLineResponse {

    private Long id;
    private String name;
    private String color;
    private List<SubwaySection> sections;

    public SubwayLineResponse(Long id, String name, String color, List<SubwaySection> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public Long getId() {
        return id;
    }

    public String getName() { return name; }

    public String getColor() { return color; }

    public List<SubwaySection> getSections() { return sections; }
}