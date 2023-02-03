package subway.fixture;

import subway.domain.Section;

import static subway.fixture.StationFixture.*;

public abstract class SectionFixture {
    public static Section 강남역_역삼역_구간 = new Section(강남역, 역삼역, 10);
    public static Section 역삼역_교대역_구간 = new Section(역삼역, 교대역, 10);
    public static Section 교대역_양재역_구간 = new Section(교대역, 양재역, 10);
}
