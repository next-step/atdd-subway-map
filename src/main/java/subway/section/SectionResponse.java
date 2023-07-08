package subway.section;

import subway.StationResponse;

public class SectionResponse {
  private long id;
  private StationResponse upStation;
  private StationResponse downStation;
  private long distance;

  public SectionResponse() {
  }

  public SectionResponse(Section section) {
    this.id = section.getId();
    this.upStation = new StationResponse(section.getUpStation());
    this.downStation = new StationResponse(section.getDownStation());
    this.distance = section.getDistance();
  }

  public long getId() {
    return id;
  }

  public StationResponse getUpStation() {
    return upStation;
  }

  public StationResponse getDownStation() {
    return downStation;
  }

  public long getDistance() {
    return distance;
  }
}
