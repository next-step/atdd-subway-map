package subway.section;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import subway.station.Station;

@Entity
public class Section {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Station upStation;

  @ManyToOne
  private Station downStation;

  private Long distance;

  public Long getId() {
    return id;
  }

  public Station getUpStation() {
    return upStation;
  }

  public Station getDownStation() {
    return downStation;
  }

  public Long getDistance() {
    return distance;
  }

  public Section() {}

  public Section(Station upStation, Station downStation, Long distance) {
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }
}
