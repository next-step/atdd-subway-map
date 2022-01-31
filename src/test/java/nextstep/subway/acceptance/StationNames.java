package nextstep.subway.acceptance;

public enum StationNames {
  강남역("강남역"),
  역삼역("역삼역"),
  양재역("양재역"),
  선릉역("선릉역");

  private final String stationName;

  StationNames(String stationName) {
    this.stationName = stationName;
  }

  public String stationName(){
    return stationName;
  }
}
