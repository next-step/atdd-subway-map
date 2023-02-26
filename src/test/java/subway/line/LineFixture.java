package subway.line;

import subway.line.presentation.LinePatchRequest;
import subway.line.presentation.LineRequest;
import subway.line.presentation.SectionRequest;

public class LineFixture {

    public static LineRequest 신분당선_생성(Long upStationId, Long downStationId) {
        LineRequest lineRequest = new LineRequest(
                "신분당선",
                "red",
                upStationId,
                downStationId,
                10L);

        return lineRequest;
    }

    public static LineRequest 강남2호선_생성(Long upStationId, Long downStationId) {
        LineRequest lineRequest = new LineRequest(
                "강남 2호선",
                "green",
                upStationId,
                downStationId,
                20L);

        return lineRequest;
    }

    public static LinePatchRequest 노선_수정_객체_생성(String name, String color) {
        return new LinePatchRequest(name, color);
    }

    public static SectionRequest 구간_등록_객체_생성(Long upStationId, Long downStationId, long distance) {
        return new SectionRequest(
          upStationId,
          downStationId,
          distance
        );
    }
}
