package subway.line;

import java.util.List;
import subway.station.StationResponse;

public class SubwayLineResponse {
  private Long id;
  private String color;
  private String name;
  private List<StationResponse> stations;

  public SubwayLineResponse() {}

  public SubwayLineResponse (SubwayLine subwayLine) {
    this.id = subwayLine.getId();
    this.color = subwayLine.getColor();
    this.name = subwayLine.getName();
  }


  public Long getId() {
    return id;
  }

  public String getColor() {
    return color;
  }

  public String getName() {
    return name;
  }

  public List<StationResponse> getStations() {
    return stations;
  }
}
