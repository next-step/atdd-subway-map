package subway.section;

import subway.station.Station;

public class SectionResponse {

  private final Long id;
  private final Station upStation;
  private final Station downStation;

  public SectionResponse(Long id, Station upStation, Station downStation) {
    this.id = id;
    this.upStation = upStation;
    this.downStation = downStation;
  }

  public static SectionResponse from(Section section) {
    return new SectionResponse(section.getId(), section.getUpStation(), section.getDownStation());
  }

  public Long getId() {
    return id;
  }

  public Station getUpStation() {
    return upStation;
  }

  public Station getDownStation() {
    return downStation;
  }
}
