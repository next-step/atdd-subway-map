package subway.line.infra;

import java.util.List;
import subway.line.domain.Line;

public interface LineRepository {
    Line save(Line line);
    List<Line> findAll();
}
