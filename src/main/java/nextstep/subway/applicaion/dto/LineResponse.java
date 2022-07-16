package nextstep.subway.applicaion.dto;

import java.util.Arrays;
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
    StationResponse upStationResponse = StationResponse.createStationResponse(line.getUpStation().getId(), line.getUpStation().getName());
    StationResponse downStationResponse = StationResponse.createStationResponse(line.getDownStation().getId(), line.getDownStation().getName());

    return LineResponse.builder()
        .id(line.getId())
        .name(line.getName())
        .color(line.getColor())
        .stations(Arrays.asList(upStationResponse, downStationResponse))
        .build();
  }
}
