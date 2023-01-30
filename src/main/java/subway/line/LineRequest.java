package subway.line;

public class LineRequest {

  private String name;
  private String inbound;
  private String outbound;

  public String getName() {
    return name;
  }

  public String getInbound() {
    return inbound;
  }

  public String getOutbound() {
    return outbound;
  }

  public LineRequest(String name, String inbound, String outbound) {
    this.name = name;
    this.inbound = inbound;
    this.outbound = outbound;
  }
}
