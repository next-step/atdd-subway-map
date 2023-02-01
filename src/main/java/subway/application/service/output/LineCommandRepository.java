package subway.application.service.output;

import subway.domain.Line;

public interface LineCommandRepository {

    Long createLine(Line line);

    void updateLine(Line line);

    void deleteLine(Long lineId);

}
