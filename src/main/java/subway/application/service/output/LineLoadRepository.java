package subway.application.service.output;

import subway.domain.LineDomain;

import java.util.List;
import java.util.Optional;

public interface LineLoadRepository {

    Optional<LineDomain> loadLine(Long createdLineId);

    List<LineDomain> loadLines();

}
