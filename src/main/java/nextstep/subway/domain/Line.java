package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Enumerated(value = EnumType.STRING)
  private Color color;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "upStation_id")
  private Station upStation;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "downStation_id")
  private Station downStation;

  private int distance;

  public Line() {}

  public Line(String name, Color color, Station upStation, Station downStation, int distance) {
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

  public Color getColor() {
    return color;
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
