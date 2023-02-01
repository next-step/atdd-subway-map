package subway.line.station;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subway.line.Line;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {
    @Query(value = "update LineStation s set s.line = :line where s.id in :ids")
    @Modifying(clearAutomatically = true)
    void updateLineByIds(@Param("ids") List<Long> stationIds, @Param("line") Line line);
}
