package subway.application.repository;

import subway.domain.LineCreateDomain;
import subway.domain.LineDomain;

import java.util.List;
import java.util.Optional;

public interface LineRepositoryPort {

    Long createLine(LineCreateDomain lineCreateDomain);

    Optional<LineDomain> loadLine(Long createdLineId);

    List<LineDomain> loadLines();

}
