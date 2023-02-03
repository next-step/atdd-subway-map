package subway.fixture;

import subway.domain.Station;

public abstract class StationFixture {
    public static final Station 강남역 = new Station(1L, "강남역");
    public static final Station 역삼역 = new Station(2L, "역삼역");
    public static final Station 교대역 = new Station(3L, "교대역");
    public static final Station 양재역 = new Station(4L, "양재역");
    public static final Station 판교역 = new Station(5L, "판교역");
}
