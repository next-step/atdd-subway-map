package nextstep.subway.acceptance;

import nextstep.subway.applicaion.dto.LineRequest;

import static nextstep.subway.acceptance.StationFixture.*;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청_응답;

public class LineFixture {

    public static final String 일호선_이름 = "redLine";
    public static final String 일호선_색상 = "red";
    public static final String 이호선_이름 = "blueLine";
    public static final String 이호선_색상 = "blue";
    public static final LineRequest FIXTURE_1호선 = LineRequest.of(
            일호선_이름, 일호선_색상, 지하철_역_생성_요청_응답(FIXTURE_시청역).getId(), 지하철_역_생성_요청_응답(FIXTURE_서울역).getId(), 0
    );
    public static final LineRequest FIXTURE_2호선 = LineRequest.of(
            이호선_이름, 이호선_색상, 지하철_역_생성_요청_응답(FIXTURE_강남역).getId(), 지하철_역_생성_요청_응답(FIXTURE_역삼역).getId(), 0
    );

}
