package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class LineReadResponse extends BaseLineResponse{

  private final List<String> stations;

  public LineReadResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate, List<String> stations) {
    super(id, name, color, createdDate, modifiedDate);
    this.stations = Collections.unmodifiableList(stations);
  }

  public List<String> getStations() {
    return stations;
  }
}
