package subway.line;

import static javax.persistence.FetchType.LAZY;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import subway.section.Section;
import subway.station.Station;

@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String color;

  @ManyToOne
  private Station upStation; // 상행역

  @ManyToOne
  private Station downStation; // 하행역

  @OneToMany(fetch = LAZY)
  private List<Section> section;

  private Long distance;

  public Line() {}

  public Line(String name, String color, Station upStation, Station downStation,
      Long distance) {
    this.name = name;
    this.color = color;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Station getUpStation() {
    return upStation;
  }

  public Station getDownStation() {
    return downStation;
  }

  public String getColor() {
    return color;
  }

  public Long getDistance() {
    return distance;
  }

  public List<Section> getSection() {
    return section;
  }

  public Line updateLine(String name, String color) {
    this.name = name;
    this.color = color;
    return this;
  }
}
