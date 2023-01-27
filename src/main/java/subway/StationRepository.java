package subway;

import java.util.List;

public interface StationRepository {
    Station save(Station station);

    List<Station> findAll();

    void deleteById(Long id);
}
