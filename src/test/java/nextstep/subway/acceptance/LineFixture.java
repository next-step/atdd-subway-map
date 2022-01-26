package nextstep.subway.acceptance;

import nextstep.subway.applicaion.dto.LineRequest;

public class LineFixture {

    public static final String 일호선_이름 = "redLine";
    public static final String 일호선_색상 = "red";
    public static final String 이호선_이름 = "blueLine";
    public static final String 이호선_색상 = "blue";
    public static final LineRequest FIXTURE_1호선 = LineRequest.of(일호선_이름, 일호선_색상, null, null, 0);
    public static final LineRequest FIXTURE_2호선 = LineRequest.of(이호선_이름, 이호선_색상, null, null, 0);

}
