package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

	Optional<Section> findSectionByUpStation(@Param("up_station_id") Station upStation);
}
