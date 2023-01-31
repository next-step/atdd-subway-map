package subway.application.service.input;

import subway.domain.LineCreateDto;
import subway.domain.LineUpdateDto;

public interface LineCommandUseCase {

    Long createLine(LineCreateDto lineCreateDto);

    void updateLine(LineUpdateDto toDomain);

    void deleteLine(Long lineId);

}
