package subway;

public enum MockStations {
  서울대입구역("서울대입구역"),
  봉천역("봉천역"),
  강남역("강남역");

  private final String value;

  MockStations(String name) {
    this.value = name;
  }

  public String getValue() {
    return value;
  }
}
