package subway.line.dto;

import subway.line.Line;
import subway.section.Section;
import subway.station.Station;

public class CreateLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public CreateLineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toEntity() {
        Section section = Section.builder().upStation(Station.saveId(upStationId)).downStation(Station.saveId(downStationId)).distance(distance).build();
        Line line = new Line(name, color);
        line.addSection(section);
        return line;
    }
}
