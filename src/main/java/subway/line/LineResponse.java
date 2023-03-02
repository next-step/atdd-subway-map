package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import subway.station.Station;
import subway.station.StationResponse;

public class LineResponse {

  private final Long id;
  private final String name;
  private final String color;
  private final List<StationResponse> stations;

  public LineResponse(Long id, String name, String color, List<Station> stations) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.stations = stations.stream().map(s -> new StationResponse(s.getId(), s.getName())).collect(Collectors.toList());
  }

  public static LineResponse from(Line line) {
    return new LineResponse(
        line.getId(),
        line.getName(),
        line.getColor(),
        line.getStations()
    );
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public List<StationResponse> getStations() {
    return stations;
  }
}
