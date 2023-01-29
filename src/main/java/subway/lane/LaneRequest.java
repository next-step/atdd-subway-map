package subway.lane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import subway.station.Station;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LaneRequest {

  private String name;
  private Station inbound;
  private Station outbound;

  public String getName() {
    return name;
  }

  public Station getInbound() {
    return inbound;
  }

  public Station getOutbound() {
    return outbound;
  }

  public LaneRequest(String name, Station inbound, Station outbound) {
    this.name = name;
    this.inbound = inbound;
    this.outbound = outbound;
  }
}
