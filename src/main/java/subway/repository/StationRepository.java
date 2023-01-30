package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.repository.entity.Station;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findAllByIdIn(List<Long> ids);
}