package subway.section;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(value = "select s from Section s where s.upStation.id = :upStationId and s.downStation.id = :downStationId")
    Optional<Section> findSectionByUpStationIdAndDownStationId(
        @Param("upStationId") Long upStationId,
        @Param("downStationId") Long downStationId
    );
}
