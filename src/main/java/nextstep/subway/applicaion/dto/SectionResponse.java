package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

public class SectionResponse {
    private Station upStation;
    private Station downStation;
    private int distance;

    public SectionResponse(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public SectionResponse(Section section) {
        this(section.getUpStation(), section.getDownStation(), section.getDistance());
    }
}
