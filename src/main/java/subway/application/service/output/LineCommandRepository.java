package subway.application.service.output;

import subway.domain.Line;
import subway.domain.LineCreateDto;
import subway.domain.LineUpdateDto;

public interface LineCommandRepository {

    Long createLine(Line line);

    void updateLine(Line line);

    void deleteLine(Long lineId);

}
