package nextstep.subway.applicaion.dto;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;

public class LineResponse {

  private Long id;

  private String name;

  private Color color;

  private List<StationResponse> stations;

  public LineResponse() {}

  public LineResponse(Long id, String name, Color color, List<StationResponse> stations) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.stations = stations;
  }

  public static LineResponse createResponse(Line line) {
    StationResponse upStationResponse = new StationResponse(line.getUpStation().getId(), line.getUpStation().getName());
    StationResponse downStationResponse = new StationResponse(line.getDownStation().getId(), line.getDownStation().getName());

    return new LineResponse(line.getId(), line.getName(), line.getColor(), Arrays.asList(upStationResponse, downStationResponse));
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public List<StationResponse> getStations() {
    return stations;
  }
}
