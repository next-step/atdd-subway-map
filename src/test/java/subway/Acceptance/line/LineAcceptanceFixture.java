package subway.Acceptance.line;

import subway.dto.LineRequest;
import subway.dto.LineSectionRequest;

public class LineAcceptanceFixture {
    public static final LineRequest 신분당선_생성_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
    public static final LineRequest 분당선_생성_요청 = new LineRequest("분당선", "bg-red-500", 1L, 2L, 5);
    public static final LineRequest 당당선_수정_요청 = new LineRequest("분당선", "bg-red-500");
    public static final LineSectionRequest 구간_등록_요청_성공 = new LineSectionRequest(2L, 3L, 10);
    public static final LineSectionRequest 구간_등록_요청_실패1 = new LineSectionRequest(3L, 3L, 10);
    public static final LineSectionRequest 구간_등록_요청_실패2 = new LineSectionRequest(2L, 1L, 10);
}
