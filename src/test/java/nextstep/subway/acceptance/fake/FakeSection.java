package nextstep.subway.acceptance.fake;

import nextstep.subway.domain.Section;

public class FakeSection {
    public static Section 강남_선릉_구간 = new Section(FakeStation.강남역_ID(), FakeStation.선릉역_ID(), 10);
    public static Section 선릉_영통_구간 = new Section(FakeStation.선릉역_ID(), FakeStation.영통역_ID(), 10);
    public static Section 선릉_강남_구간 = new Section(FakeStation.선릉역_ID(), FakeStation.강남역_ID(), 10);

    public static Long 강남_선릉_구간_상행역_ID() {
        return 강남_선릉_구간.getUpStationId();
    }

    public static Long 강남_선릉_구간_하행역_ID() {
        return 강남_선릉_구간.getDownStationId();
    }

    public static Long 선릉_영통_구간_상행역_ID() {
        return 선릉_영통_구간.getUpStationId();
    }

    public static Long 선릉_영통_구간_하행역_ID() {
        return 선릉_영통_구간.getDownStationId();
    }

    public static Long 선릉_강남_구간_상행역_ID() {
        return 선릉_강남_구간.getUpStationId();
    }

    public static Long 선릉_강남_구간_하행역_ID() {
        return 선릉_강남_구간.getDownStationId();
    }

}
