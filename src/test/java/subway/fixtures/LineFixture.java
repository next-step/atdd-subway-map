package subway.fixtures;

import subway.controller.dto.CreateLineRequest;
import subway.domain.command.LineCommand;
import subway.domain.entity.line.Line;

import java.util.UUID;

public class LineFixture {
    public static Line prepareRandom(Long upStationId, Long downStationId) {
        return Line.init(new LineCommand.CreateLine(UUID.randomUUID().toString(), UUID.randomUUID().toString(), upStationId, downStationId, 10L));
    }

    public static Line prepareLineOne(Long upStationId, Long downStationId) {
        return Line.init(new LineCommand.CreateLine("1호선", "#0052A4", upStationId, downStationId, 10L));
    }

    public static CreateLineRequest prepareLineOneCreateRequest(Long upStationId, Long downStationId) {
        return new CreateLineRequest("1호선", "#0052A4", upStationId, downStationId, 10L);
    }

    public static CreateLineRequest prepareLineTwoCreateRequest(Long upStationId, Long downStationId) {
        return new CreateLineRequest("2호선", "#00A84D", upStationId, downStationId, 12L);
    }
}
