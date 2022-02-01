package nextstep.subway.domain.factory;

import nextstep.subway.domain.line.dto.LineRequest;

public class DtoFactory {
    public static LineRequest createLineRequest(String name, String color,
                                                Long upStationId, Long downStationId, int distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }
}
