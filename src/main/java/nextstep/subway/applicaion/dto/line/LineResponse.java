package nextstep.subway.applicaion.dto.line;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private Station upStation;
    private Station downStation;
    private Long distance;

    public LineResponse(Line entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();

        List<Section> sections = entity.getSections();
        this.upStation = sections.get(0).getUpStation();
        this.downStation = sections.get(sections.size() - 1).getDownStation();

        this.distance = entity.getDistance();
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
