package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.service.dto.Stations;
import subway.repository.entity.Station;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findByIdIn(List<Long> ids);
}
