package nextstep.subway.domain.station;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    List<Station> findAll();

    Optional<Station> findById(Long aLong);

    <S extends Station> S save(S entity);

    void deleteById(Long aLong);
}
