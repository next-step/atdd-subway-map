package subway.dto;

import java.util.List;

import lombok.Getter;
import subway.domain.Line;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final List<SectionResponse> sections;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, List<SectionResponse> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.sections = sections;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(),
                                line.getName(),
                                line.getColor(),
                                StationResponse.createStationsResponse(line.getSections()),
                                SectionResponse.createSectionResponse(line.getSections()));
    }
}
