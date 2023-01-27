package subway.station.domain;

import java.util.List;

public interface StationRepository {
    Station save(Station station);

    List<Station> findAll();

    void deleteById(Long id);
}
