package subway.application.service;

import subway.domain.LineCreateDomain;

public interface LineUseCase {

    Long createLine(LineCreateDomain lineCreateDomain);

}
