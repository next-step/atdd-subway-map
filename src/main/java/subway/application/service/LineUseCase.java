package subway.application.service;

import subway.domain.LineCreateDomain;

public interface LineUseCase {

    void createLine(LineCreateDomain lineCreateDomain);

}
