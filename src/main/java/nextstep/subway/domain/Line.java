package nextstep.subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Embedded
  private Color color;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "upStation_id")
  private Station upStation;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "downStation_id")
  private Station downStation;

  private int distance;

  public Line() {}

  @Builder
  public Line(String name, Color color, Station upStation, Station downStation, int distance) {
    this.name = name;
    this.color = color;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public void changeName(String name) {
    this.name = name;
  }

  public void changeColor(Color color) {
    this.color = color;
  }
}
