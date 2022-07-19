package nextstep.subway.applicaion.dto;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.applicaion.dto.StationResponse.createStationResponse;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;

@Getter
public class LineResponse {

  private Long id;

  private String name;

  private String color;

  private List<StationResponse> stations;

  public LineResponse() {}

  @Builder
  public LineResponse(Long id, String name, Color color, List<StationResponse> stations) {
    this.id = id;
    this.name = name;
    this.color = color.getColor();
    this.stations = stations;
  }

  public static LineResponse createResponse(Line line) {
    return LineResponse.builder()
        .id(line.getId())
        .name(line.getName())
        .color(line.getColor())
        .stations(getStationResponse(line))
        .build();
  }

  private static List<StationResponse> getStationResponse(Line line) {
    return line.getSections().getAllStations().stream()
        .map(station -> createStationResponse(station.getId(), station.getName()))
        .collect(toList());
  }
}
