package subway.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findAllByIdIn(List<Long> id);
}