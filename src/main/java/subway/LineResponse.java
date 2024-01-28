package subway;

import java.util.List;

public class LineResponse {

  private final Long id;
  private final String name;
  private final String color;
  private final List<StationResponse> stations;

  public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.stations = stations;
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

  public static LineResponse from(final Line line, final StationResponse upStation, final StationResponse downStation) {
    return new LineResponse(
        line.getId(),
        line.getName(),
        line.getColor(),
        List.of(
          upStation,
          downStation
        )
    );
  }

}
