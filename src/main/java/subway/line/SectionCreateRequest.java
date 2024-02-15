package subway.line;

public class SectionCreateRequest {

  private final Long downStationId;
  private final Long upStationId;
  private final int distance;

  public SectionCreateRequest(Long downStationId, Long upStationId, int distance) {
    this.downStationId = downStationId;
    this.upStationId = upStationId;
    this.distance = distance;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public int getDistance() {
    return distance;
  }

}
