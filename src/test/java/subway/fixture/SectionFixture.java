package subway.fixture;

import subway.controller.request.SectionRequest;

import static subway.fixture.StationFixture.교대역_ID;
import static subway.fixture.StationFixture.양재역_ID;
import static subway.fixture.StationFixture.역삼역_ID;

public abstract class SectionFixture {
    public static SectionRequest 역삼역_교대역_구간_요청 =  SectionRequest.builder().upStationId(역삼역_ID).downStationId(교대역_ID).distance(10).build();
    public static SectionRequest 교대역_양재역_구간_요청 =  SectionRequest.builder().upStationId(교대역_ID).downStationId(양재역_ID).distance(10).build();
    public static SectionRequest 양재역_양재역_구간_요청 =  SectionRequest.builder().upStationId(양재역_ID).downStationId(양재역_ID).distance(10).build();
    public static SectionRequest 강남역_양재역_구간_요청 =  SectionRequest.builder().upStationId(양재역_ID).downStationId(양재역_ID).distance(10).build();
}
