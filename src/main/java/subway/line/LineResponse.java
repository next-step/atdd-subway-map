package subway.line;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import subway.station.StationResponse;

import java.util.Arrays;
import java.util.List;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
