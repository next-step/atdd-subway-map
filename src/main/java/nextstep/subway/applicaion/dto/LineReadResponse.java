package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import nextstep.subway.domain.Line;

public class LineReadResponse extends BaseLineResponse {

    private final List<String> stations;

    public LineReadResponse(
            Long id,
            String name,
            String color,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate,
            List<String> stations) {
        super(id, name, color, createdDate, modifiedDate);
        this.stations = Collections.unmodifiableList(stations);
    }

    public List<String> getStations() {
        return stations;
    }

    public static LineReadResponse of(Line line, List<String> stations) {
        return new LineReadResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate(),
                stations);
    }
}
