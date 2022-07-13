package nextstep.subway.domain.line;

import java.util.List;
import java.util.Optional;

public interface LineRepository {
    List<Line> findAll();

    Optional<Line> findById(Long aLong);

    <S extends Line> S save(S entity);

    void deleteById(Long aLong);
}
