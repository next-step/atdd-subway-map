package subway.dto.response;

import lombok.Getter;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Getter
public class SectionResponse {

    private Line line;
    private Station upStation;
    private Station downStation;
    private int distance;

    public SectionResponse(Line line, Section section) {
        this.line = line;
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.distance = section.getDistance();
    }
}
