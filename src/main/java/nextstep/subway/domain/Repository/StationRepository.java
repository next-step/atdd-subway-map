package nextstep.subway.domain.Repository;

import nextstep.subway.domain.Entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    Station findByName(@Param("name") String name);
}