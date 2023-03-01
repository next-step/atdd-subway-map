package subway.section;

import subway.station.Station;
import subway.station.StationResponse;

public class SectionResponse {

  private final Long id;
  private final StationResponse upStation;
  private final StationResponse downStation;

  public SectionResponse(Long id, Station upStation, Station downStation) {
    this.id = id;
    this.upStation = StationResponse.from(upStation);
    this.downStation = StationResponse.from(downStation);
  }

  public static SectionResponse from(Section section) {
    return new SectionResponse(section.getId(), section.getUpStation(), section.getDownStation());
  }

  public Long getId() {
    return id;
  }

  public StationResponse getUpStation() {
    return upStation;
  }

  public StationResponse getDownStation() {
    return downStation;
  }
}
