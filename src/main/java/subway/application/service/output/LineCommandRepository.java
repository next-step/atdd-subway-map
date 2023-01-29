package subway.application.service.output;

import subway.domain.LineCreateDomain;
import subway.domain.LineUpdateDomain;

public interface LineCommandRepository {

    Long createLine(LineCreateDomain lineCreateDomain);

    void updateLine(LineUpdateDomain toDomain);

    void deleteLine(Long lineId);

}
