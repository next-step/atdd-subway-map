package subway.line;

import subway.station.Station;

public class LineResponse {

  private Long id;
  private String name;
  private Station inboundStation;
  private Station outboundStation;

  public LineResponse(Long id, String name, Station inboundStation, Station outboundStation) {
    this.id = id;
    this.name = name;
    this.inboundStation = inboundStation;
    this.outboundStation = outboundStation;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Station getInboundStation() {
    return inboundStation;
  }

  public Station getOutboundStation() {
    return outboundStation;
  }
}
