package subway.application.repository;

import subway.domain.LineCreateDomain;

public interface LineRepositoryPort {

    void createLine(LineCreateDomain lineCreateDomain);

}
