package subway.line;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Builder
@AllArgsConstructor
public class SubwayLineRequest {

  private String name;
  private String color;
  private Long upStationId;
  private Long downStationId;
  private Long distance;

  public SubwayLineRequest() {}

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public Long getDistance() {
    return distance;
  }

  @JsonIgnore
  public SubwayLine toEntity() {
    return SubwayLine.builder()
        .color(this.color)
        .name(this.name)
        .build();
  }
}
