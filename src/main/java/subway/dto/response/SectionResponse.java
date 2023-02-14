package subway.dto.response;

import lombok.Getter;
import subway.domain.Line;
import subway.domain.Section;

@Getter
public class SectionResponse {

    private final LineResponse line;
    private final StationResponse upStation;
    private final StationResponse downStation;
    private final int distance;

    public SectionResponse(Line line, Section section) {
        this.line = LineResponse.of(line);
        this.upStation = StationResponse.of(section.getUpStation());
        this.downStation = StationResponse.of(section.getDownStation());
        this.distance = section.getDistance();
    }
}
