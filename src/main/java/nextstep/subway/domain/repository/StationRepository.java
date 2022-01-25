package nextstep.subway.domain.repository;

import nextstep.subway.domain.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Override
    List<Station> findAll();

    List<Station> findByName(final String name);
}
