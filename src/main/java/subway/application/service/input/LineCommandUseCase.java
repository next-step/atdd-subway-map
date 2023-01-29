package subway.application.service.input;

import subway.domain.LineCreateDomain;
import subway.domain.LineUpdateDomain;

public interface LineCommandUseCase {

    Long createLine(LineCreateDomain lineCreateDomain);

    void updateLine(LineUpdateDomain toDomain);

    void deleteLine(Long lineId);

}
