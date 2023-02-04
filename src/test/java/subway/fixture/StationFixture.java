package subway.fixture;

import subway.domain.Station;

public abstract class StationFixture {
    public static final long 강남역_ID = 1L;
    public static final long 역삼역_ID = 2L;
    public static final long 교대역_ID = 3L;
    public static final long 양재역_ID = 4L;
    public static final long 미존재역_ID = 1000L;

    public static final String 강남역_이름 = "강남역";
    public static final String 역삼역_이름 = "역삼역";
    public static final String 교대역_이름 = "교대역";
    public static final String 양재역_이름 = "양재역";

    public static final Station 강남역 = new Station(강남역_ID, 강남역_이름);
    public static final Station 역삼역 = new Station(역삼역_ID, 역삼역_이름);
    public static final Station 교대역 = new Station(교대역_ID, 교대역_이름);
    public static final Station 양재역 = new Station(양재역_ID, 양재역_이름);
}
