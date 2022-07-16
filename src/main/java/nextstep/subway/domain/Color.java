package nextstep.subway.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Color {

  private String color;

  public Color() {}

  public Color(String color) {
    this.color = color;
  }

  public void changeColor(String color) {
    this.color = color;
  }

  private void setColor(String color) {
    this.color = color;
  }
}
