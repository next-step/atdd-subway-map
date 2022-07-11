package nextstep.subway.domain;

public enum Color {
  BLUE(1),
  GREEN(2),
  ORANGE(3),
  SKY(4)
  ;

  private int value;

  Color(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }
}
