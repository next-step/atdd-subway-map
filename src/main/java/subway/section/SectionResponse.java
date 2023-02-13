package subway.section;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.line.Line;
import subway.line.LineResponse;
import subway.station.StationResponse;

@AllArgsConstructor
@Getter
public class SectionResponse {

    private Long id;
    private LineResponse line;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;

    public static SectionResponse of(Section section, Line line) {
        LineResponse lineResponse = LineResponse.of(line.getId(), line.getName(), line.getColor(),
            List.of(line.getUpStation(), line.getDownStation()), line.getUpStation(),
            line.getDownStation());

        return new SectionResponse(section.getId(), lineResponse, StationResponse.of(section.getUpStation()),
            StationResponse.of(section.getDownStation()), section.getDistance());
    }
}
