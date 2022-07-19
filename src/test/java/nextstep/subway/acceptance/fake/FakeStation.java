package nextstep.subway.acceptance.fake;

import nextstep.subway.domain.Station;

public class FakeStation {
    public static Station 강남역 = new Station(1L, "강남역");
    public static Station 선릉역 = new Station(2L, "선릉역");
    public static Station 영통역 = new Station(3L, "영통역");
    public static Station 판교역 = new Station(4L, "판교역");

    public static Long 판교역_ID() {
        return 판교역.getId();
    }

    public static Long 선릉역_ID() {
        return 선릉역.getId();
    }

    public static Long 영통역_ID() {
        return 영통역.getId();
    }

    public static Long 강남역_ID() {
        return 강남역.getId();
    }
}
