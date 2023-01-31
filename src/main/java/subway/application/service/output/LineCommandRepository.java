package subway.application.service.output;

import subway.domain.LineCreateDto;
import subway.domain.LineUpdateDto;

public interface LineCommandRepository {

    Long createLine(LineCreateDto lineCreateDto);

    void updateLine(LineUpdateDto toDomain);

    void deleteLine(Long lineId);

}
