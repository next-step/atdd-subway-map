package subway.section;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.line.Line;
import subway.line.LineResponse;
import subway.station.Station;
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
        LineResponse lineResponse = LineResponse.of(
            line.getId(),
            line.getName(),
            line.getColor(),
            line.getAllStations(),
            line.getUpStation(),
            line.getDownStation()
        );

        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        return new SectionResponse(
            section.getId(),
            lineResponse,
            StationResponse.of(upStation),
            StationResponse.of(downStation),
            section.getDistance());
    }
}
