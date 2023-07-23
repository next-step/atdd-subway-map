package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class SubwayLineEditRequest {

  private String name;
  private String color;

  public SubwayLineEditRequest() {}

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

}
