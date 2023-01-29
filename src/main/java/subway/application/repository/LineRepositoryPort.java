package subway.application.repository;

import subway.domain.LineCreateDomain;

public interface LineRepositoryPort {

    Long createLine(LineCreateDomain lineCreateDomain);

}
