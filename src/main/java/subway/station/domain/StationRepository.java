package subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.exception.StationNotFoundException;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new StationNotFoundException(id));
    }
}