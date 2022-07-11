package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
  private Station upStationId;

  @OneToOne(fetch = FetchType.LAZY)
  private Station downStationId;

  private int distance;

  public Line() {}

  public Line(String name, Color color, Station upStationId, Station downStationId, int distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
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

  public Station getUpStationId() {
    return upStationId;
  }

  public Station getDownStationId() {
    return downStationId;
  }

  public int getDistance() {
    return distance;
  }
}
