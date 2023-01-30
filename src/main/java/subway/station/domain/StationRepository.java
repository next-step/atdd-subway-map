package subway.station.domain;

import java.util.List;

public interface StationRepository {

    List<Station> findAllByIdIn(List<Long> ids);

    Station save(Station station);

    List<Station> findAll();

    void deleteById(Long id);
}
