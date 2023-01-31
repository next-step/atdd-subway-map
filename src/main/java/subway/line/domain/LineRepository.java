package subway.line.domain;

import java.util.List;

public interface LineRepository {
    Line save(Line line);

    List<Line> findAll();
}
