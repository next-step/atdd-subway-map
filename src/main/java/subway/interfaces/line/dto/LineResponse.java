package subway.interfaces.line.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.interfaces.station.dto.StationResponse;
import subway.domain.line.entity.Line;

@Getter
@RequiredArgsConstructor
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final StationResponse upStation;
    private final StationResponse downStation;
    public static LineResponse from(Line line) {
        StationResponse upStation = new StationResponse(line.getUpStation().getId(), line.getUpStation().getName());
        StationResponse downStation = new StationResponse(line.getDownStation().getId(), line.getDownStation().getName());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), upStation, downStation);
    }

}
