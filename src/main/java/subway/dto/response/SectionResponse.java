package subway.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.domain.Line;
import subway.domain.Station;

@Getter
@Builder
public class SectionResponse {

    private Line line;
    private Station upStation;
    private Station downStation;
    private int distance;

    public SectionResponse(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }
}
