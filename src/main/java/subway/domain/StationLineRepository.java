package subway.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subway.domain.StationLine;

public interface StationLineRepository extends JpaRepository<StationLine, Long> {
	@Override
	@Query("SELECT SL FROM StationLine SL LEFT JOIN FETCH SL.sections")
	List<StationLine> findAll();

	@Override
	@Query("SELECT SL FROM StationLine SL LEFT JOIN FETCH SL.sections WHERE SL.lineId =:lineId")
	Optional<StationLine> findById(@Param("lineId") Long lineId);
}
