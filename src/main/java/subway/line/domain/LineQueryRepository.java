package subway.line.domain;

import java.util.List;
import java.util.Optional;

public interface LineQueryRepository {

    List<Line> findAll();

    Optional<Line> findById(Long lineId);
}
