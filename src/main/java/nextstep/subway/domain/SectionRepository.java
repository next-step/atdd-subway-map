package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

	Section findSectionByUpStation(@Param("up_station_id") Station upStation);
	Section findSectionByDownStation(@Param("down_station_id") Station downStation);
	Optional<Section> findByLineAndDownStation(@Param("line") Line line, @Param("down_station_id") Station downStation);
}
