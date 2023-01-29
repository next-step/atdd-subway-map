package subway;

import subway.station.Station;

public class Mocks {

  public static class MockStation {
    public static Station 서울대입구역 = new Station("서울대입구역");
    public static Station 봉천역 = new Station("봉천역");
    public static Station 강남역 = new Station("강남역");
    public static Station 신사역 = new Station("신사역");
  }

  public static class MockLane {
    public static String 서울2호선 = "서울2호선";
    public static String 신분당선 = "신분당선";
  }
}
