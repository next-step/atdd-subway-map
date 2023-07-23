package subway.line;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import subway.station.Station;
import subway.station.StationResponse;

public class LineResponse {
  private Long id;
  private String color;
  private String name;
  private List<StationResponse> stations = Collections.emptyList();

  public LineResponse() {}

  public LineResponse(SubwayLine subwayLine) {
    this.id = subwayLine.getLineId();
    this.color = subwayLine.getColor();
    this.name = subwayLine.getName();
  }

  public LineResponse(SubwayLine subwayLine, List<Station> stations) {
    this(subwayLine);
    this.stations = stations.stream()
        .map(StationResponse::new)
        .collect(Collectors.toUnmodifiableList());
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
