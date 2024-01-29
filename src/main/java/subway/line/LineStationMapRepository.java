package subway.line;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineStationMapRepository extends JpaRepository<LineStationMap, Long> {
	List<LineStationMap> findAllByLineId(Long lineId);
}
