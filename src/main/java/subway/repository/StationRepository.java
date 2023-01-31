package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Station;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByIdInOrderById(List<Long> id);
}