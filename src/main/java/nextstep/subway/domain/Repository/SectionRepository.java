package nextstep.subway.domain.Repository;

import nextstep.subway.domain.Entity.Section;
import nextstep.subway.domain.Entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

	Optional<Section> findByDownStation(@Param("down_station_id")Station station);
}
