package subway.section;

public class SectionCreateRequest {

  private final String downStationId;
  private final String upStationId;
  private final Long distance;

  public SectionCreateRequest(String downStationId, String upStationId, Long distance) {
    this.downStationId = downStationId;
    this.upStationId = upStationId;
    this.distance = distance;
  }

  public String getDownStationId() {
    return downStationId;
  }

  public String getUpStationId() {
    return upStationId;
  }

  public Long getDistance() {
    return distance;
  }
}
