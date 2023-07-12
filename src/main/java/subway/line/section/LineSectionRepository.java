package subway.line.section;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineSectionRepository extends JpaRepository<LineSection, Long> {
  List<LineSection> findByLineId(Long lineId);

  @Query(
      "SELECT ls, up_s, down_s " +
      "FROM LineSection ls " +
      "  JOIN fetch Station up_s " +
      "    ON ls.upStationId = up_s.stationId " +
      "  JOIN fetch Station down_s " +
      "    ON ls.downStationId = down_s.stationId " +
      "WHERE ls.lineId = :lineId "
  )
  List<LineSection> findByLineIdWithStation(@Param("lineId") Long lineId);
}
