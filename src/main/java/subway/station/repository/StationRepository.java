package subway.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.repository.entity.StationEntity;

public interface StationRepository extends JpaRepository<StationEntity, Long> {
}