package subway.line.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import subway.line.repository.Line;
import subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public
class LineResponse {
    Long id;
    String name;
    String color;
    List<StationResponse> stations;

    public static LineResponse from(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                Arrays.asList(
                        new StationResponse(line.getUpStation().getId(), line.getUpStation().getName()),
                        new StationResponse(line.getDownStation().getId(), line.getDownStation().getName())
                ));
    }
}
