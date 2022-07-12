package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
	List<Section> findByLineIdOrderById(long lineId);

	Optional<Section> findByDownStationId(long stationId);

}