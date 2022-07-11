package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private Station upStation;
    private Station downStation;

    public LineResponse(Line entity, Station upStation, Station downStation) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.color = entity.getColor();
        this.upStation = upStation;
        this.downStation = downStation;
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
}
