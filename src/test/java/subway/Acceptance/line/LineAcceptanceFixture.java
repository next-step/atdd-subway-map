package subway.Acceptance.line;

import subway.dto.LineRequest;
import subway.dto.LineSectionRequest;

public class LineAcceptanceFixture {
    public static final Long 강남역 = 1L;
    public static final Long 역삼역 = 2L;
    public static final Long 선릉역 = 3L;
    public static final String 빨간색 = "bg-red-500";
    public static final String 당당선 = "당당선";
    public static final Long 이호선 = 1L;

    public static LineRequest 이호선(Long upStationId, Long downStationId) {
        return new LineRequest("이호선", "bg-red-600", upStationId, downStationId, 10);
    }
    public static LineRequest 분당선(Long upStationId, Long downStationId) {
        return new LineRequest("이호선", "bg-red-600", upStationId, downStationId, 10);
    }
    public static LineRequest 당당선_수정(String name, String color) {
        return new LineRequest(name, color);
    }
    public static final LineSectionRequest 구간_등록_요청(Long upStationId, Long downStationId) {
        return new LineSectionRequest(upStationId, downStationId, 10);
    }

}
