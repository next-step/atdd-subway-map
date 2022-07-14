package nextstep.subway.domain.line;

import nextstep.subway.domain.station.Station;

public class Fixture {
    public static final Station 분당역 = new Station(1L, "분당역");
    public static final Station 강남역 = new Station(2L, "강남역");
    public static final Station 잠실역 = new Station(3L, "잠실역");
    public static final Section 구간 = new Section(null, 강남역.getId(), 분당역.getId(), 10);
}
