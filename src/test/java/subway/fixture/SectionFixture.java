package subway.fixture;

import subway.controller.request.SectionRequest;
import subway.repository.entity.Section;

import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.강남역_ID;
import static subway.fixture.StationFixture.교대역;
import static subway.fixture.StationFixture.교대역_ID;
import static subway.fixture.StationFixture.양재역;
import static subway.fixture.StationFixture.양재역_ID;
import static subway.fixture.StationFixture.역삼역;
import static subway.fixture.StationFixture.역삼역_ID;

public abstract class SectionFixture {
    public static Section 강남역_역삼역_구간 = Section.builder().upStation(강남역).downStation(역삼역).distance(10).build();
    public static Section 역삼역_교대역_구간 = Section.builder().upStation(역삼역).downStation(교대역).distance(10).build();
    public static Section 교대역_양재역_구간 = Section.builder().upStation(교대역).downStation(양재역).distance(10).build();
    public static SectionRequest 역삼역_교대역_구간_요청 = new SectionRequest(역삼역_ID, 교대역_ID, 10);
    public static SectionRequest 교대역_양재역_구간_요청 = new SectionRequest(교대역_ID, 양재역_ID, 10);
    public static SectionRequest 양재역_양재역_구간_요청 = new SectionRequest(양재역_ID, 양재역_ID, 10);
    public static SectionRequest 역삼역_강남역_구간_요청 = new SectionRequest(역삼역_ID, 강남역_ID, 10);
}
