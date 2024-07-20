package subway.line.dto;

import subway.StationResponse;
import subway.line.Line;

import java.util.List;

public class LineResponse {

  private Long id;

  private String name;

  private String color;

  private List<StationResponse> stations;

  public LineResponse() {
  }

  public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
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

  public List<StationResponse> getStations(){
    return stations;
  }

  public static LineResponse from(Line line) {
    StationResponse upStation = StationResponse.from(line.getUpStation());
    StationResponse downStation = StationResponse.from(line.getDownStation());
    return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
  }
}
