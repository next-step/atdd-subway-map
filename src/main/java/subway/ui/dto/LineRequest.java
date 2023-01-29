package subway.ui.dto;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    private LineRequest() {}

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(final String name, final String color) {
        this(name, color, 0L, 0L, 0);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Line toEntity(final List<Section> sections) {
        return new Line(this.name, this.color, Sections.from(sections));
    }

    public Section toSectionEntity(final Station upStation, final Station downStation) {
        return new Section(upStation, downStation, this.distance);
    }
}
