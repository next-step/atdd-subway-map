package subway.application.service;

import subway.domain.LineCreateDomain;
import subway.domain.LineDomain;

public interface LineUseCase {

    Long createLine(LineCreateDomain lineCreateDomain);

    LineDomain loadLine(Long createdLineId);

}
