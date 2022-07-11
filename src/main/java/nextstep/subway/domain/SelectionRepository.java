package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionRepository extends JpaRepository<Selection, Long> {
	List<Selection> findByLineIdOrderById(long lineId);

	Optional<Selection> findByDownStationId(long stationId);
}