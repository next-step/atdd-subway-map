package subway.application.repository;

import subway.domain.LineCreateDomain;

public interface LineRepository {

    void createLine(LineCreateDomain lineCreateDomain);

}
