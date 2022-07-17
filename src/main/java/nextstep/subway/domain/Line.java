package nextstep.subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  private int distance;

  public Line() {}

  @Builder
  public Line(String name, Color color, int distance) {
    this.name = name;
    this.color = color;
    this.distance = distance;
  }

  public void changeName(String name) {
    this.name = name;
  }

  public void changeColor(Color color) {
    this.color = color;
  }

  public void addDistance(int distance) {
    this.distance += distance;
  }
}
