package subway.Acceptance;

import subway.dto.LineRequest;

public class AcceptanceTestFixture {
    public static final LineRequest SHIN_BUN_DANG_LINE_REQUEST = new LineRequest("신분당선", "bg-red-600", 2L, 3L, 10);
    public static final LineRequest BUN_DANG_LINE_REQUEST = new LineRequest("분당선", "bg-red-500", 2L, 3L, 5);
}
