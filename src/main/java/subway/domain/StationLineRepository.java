package subway.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subway.domain.StationLine;

public interface StationLineRepository extends JpaRepository<StationLine, Long> {
	@Override
	@Query("SELECT SL FROM StationLine SL LEFT JOIN FETCH SL.upStation JOIN FETCH SL.downStation")
	List<StationLine> findAll();
}
