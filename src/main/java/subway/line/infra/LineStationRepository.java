package subway.line.infra;

import java.util.List;
import subway.line.domain.Line;
import subway.line.domain.LineStationConnection;

public interface LineStationRepository {
    <S extends LineStationConnection> Iterable<S> saveAll(Iterable<S> entities);
    List<LineStationConnection> findAllByLine(Line line);
}
