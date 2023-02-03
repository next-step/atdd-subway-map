package subway.Acceptance.line;

import subway.dto.LineRequest;

public class LineAcceptanceFixture {
    public static final LineRequest 신분당선_생성_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
    public static final LineRequest 분당선_생성_요청 = new LineRequest("분당선", "bg-red-500", 1L, 2L, 5);
    public static final LineRequest 당당선_수정_요청 = new LineRequest("분당선", "bg-red-500");
}
