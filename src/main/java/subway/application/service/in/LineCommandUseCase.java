package subway.application.service.in;

import subway.domain.LineCreateDomain;
import subway.domain.LineUpdateDomain;

public interface LineCommandUseCase {

    Long createLine(LineCreateDomain lineCreateDomain);

    void updateLine(LineUpdateDomain toDomain);

    void deleteLine(Long lineId);

}
