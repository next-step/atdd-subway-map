package subway.line.infra;

import java.util.List;
import java.util.Optional;
import subway.line.domain.Line;

public interface LineRepository {
    Line save(Line line);
    List<Line> findAll();
    Optional<Line> findById(Long id);
    void deleteById(Long id);
}
