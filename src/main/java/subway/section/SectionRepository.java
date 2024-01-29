package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(
            "select s " +
            "from Section s " +
            "where s in :sections " +
            "and s.upStation.id = :stationId " +
            "or s.downStation.id = :stationId"
    )
    List<Section> findDeleteSections(List<Section> sections, Long stationId);
}
