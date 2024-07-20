package subway.line.dto;

public class LineSectionAppendRequest {
  private Long upStationId;
  private Long downStationId;
  private int distance;

  public LineSectionAppendRequest() {
  }

  public LineSectionAppendRequest(Long upStationId, Long downStationId, int distance) {
    this.downStationId = downStationId;
    this.upStationId = upStationId;
    this.distance = distance;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public int getDistance() {
    return distance;
  }
}
