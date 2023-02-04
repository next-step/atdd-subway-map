package subway.fixture;

import subway.controller.request.LineRequest;

import static subway.fixture.StationFixture.강남역_ID;
import static subway.fixture.StationFixture.교대역_ID;
import static subway.fixture.StationFixture.미존재역_ID;
import static subway.fixture.StationFixture.역삼역_ID;

public abstract class LineFixture {
    public static final String 신분당선 = "신분당선";
    public static final String 분당선 = "분당선";
    public static final long 존재하지_않는_노선_ID = 1000L;
    public static final LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
    public static final LineRequest 분당선_요청 = new LineRequest("분당선", "bg-yellow-600", 강남역_ID, 역삼역_ID, 10);
    public static final LineRequest 잘못된_노선_요청 = new LineRequest("분당선", "bg-yellow-600", 교대역_ID, 미존재역_ID, 10);
    public static final LineRequest 지하철노선_수정_요청 = new LineRequest("다른 분당선", "bg-other-600", null, null, 0);
}
