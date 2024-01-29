package subway.station;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    Optional<Station> findById(Long aLong);
}
