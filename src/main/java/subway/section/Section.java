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
}
