package subway.station.domain;

import java.util.List;

public interface StationRepository {

    List<Station> findAllByIdIn(Iterable<Long> ids);

    Station save(Station station);

    List<Station> findAll();

    void deleteById(Long id);
}
