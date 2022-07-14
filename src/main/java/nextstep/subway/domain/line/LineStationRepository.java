package nextstep.subway.domain.line;

import java.util.List;
import java.util.Optional;

public interface LineStationRepository {

    List<Section> findAll();

    Optional<Section> findById(Long aLong);

    <S extends Section> S save(S entity);

    void deleteById(Long aLong);
}
