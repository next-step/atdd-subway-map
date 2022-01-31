package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;

public class LineReadResponse extends BaseLineResponse {

    private final List<StationResponse> stations;

    public LineReadResponse(
            Long id,
            String name,
            String color,
            List<StationResponse> stations,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        super(id, name, color, createdDate, modifiedDate);
        this.stations = Collections.unmodifiableList(stations);
    }

  public List<StationResponse> getStations() {
    return Collections.unmodifiableList(stations);
  }
}
