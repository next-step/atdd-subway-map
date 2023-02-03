package subway.station.domain;

import java.util.List;
import java.util.Optional;

public interface StationQueryRepository {

    List<Station> findAll();

    Optional<Station> findById(Long stationId);
}
