package nextstep.subway.applicaion.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

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

  public static LineResponse createResponse(Line line, List<Station> stations) {
    List<StationResponse> stationResponse = stations.stream()
        .map(station -> new StationResponse(station.getId(), station.getName()))
        .distinct()
        .collect(toList());

    return LineResponse.builder()
        .id(line.getId())
        .name(line.getName())
        .color(line.getColor())
        .stations(stationResponse)
        .build();
  }
}
