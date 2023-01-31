package subway.station.domain;

import java.util.List;
import java.util.Optional;

public interface StationRepository {
    Station save(Station station);

    void deleteById(Long id);

    List<Station> findAll();

    Optional<Station> findById(Long id);
}
