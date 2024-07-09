package subway.fixtures;

import subway.controller.dto.CreateLineRequest;
import subway.domain.entity.Line;

public class LineFixture {
    public static Line prepareLineOne(Long upStationId, Long downStationId) {
        return new Line("1호선", "#0052A4", upStationId, downStationId, 10L);
    }

    public static CreateLineRequest prepareLineOneCreateRequest(Long upStationId, Long downStationId) {
        return new CreateLineRequest("1호선", "#0052A4", upStationId, downStationId, 10L);
    }

    public static CreateLineRequest prepareLineTwoCreateRequest(Long upStationId, Long downStationId) {
        return new CreateLineRequest("2호선", "#00A84D", upStationId, downStationId, 12L);
    }
}
