package subway.line;

import subway.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class LineSection {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "line_id")
  private Line line;

  private Long index;

  @OneToOne
  private Station upStation;

  @OneToOne
  Station downStation;

  private int distance;

  public LineSection() {
  }

  public LineSection(Long id, Line line, Long index, Station upStation, Station downStation, int distance) {
    this.id = id;
    this.line = line;
    this.index = index;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public Long getId() {
    return id;
  }

  public Line getLine() {
    return line;
  }

  public Long getIndex() {
    return index;
  }

  public Station getUpStation() {
    return upStation;
  }

  public Station getDownStation() {
    return downStation;
  }

  public int getDistance() {
    return distance;
  }
}
