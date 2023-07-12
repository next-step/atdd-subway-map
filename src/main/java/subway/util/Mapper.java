package subway.util;

import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.model.Line;
import subway.model.Station;

public class Mapper {
    private Mapper() {
    }

    public static StationResponse toResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static LineResponse toResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getUpStation(), line.getDownStation());
    }
}
