package nextstep.subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface StationRepository extends JpaRepository<Station, Long> {
    boolean existsByName(String name);

    List<Station> findAllByIdIn(Set<Long> stationIds);
}