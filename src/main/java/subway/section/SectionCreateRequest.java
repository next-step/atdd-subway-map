package subway.section;

public class SectionCreateRequest {

  private final Long downStationId;
  private final Long upStationId;
  private final Long distance;

  public SectionCreateRequest(Long upStationId, Long downStationId, Long distance) {
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDistance() {
    return distance;
  }
}
