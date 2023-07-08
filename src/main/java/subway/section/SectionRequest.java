package subway.section;

public class SectionRequest {
  private Long downStationId;
  private Long upStationId;
  private Long distance;

  public boolean isValid() {
    if (downStationId == null) {
      return false;
    }

    if (upStationId == null) {
      return false;
    }

    if (distance == null) {
      return false;
    }

    return true;
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
