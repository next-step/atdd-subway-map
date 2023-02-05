package subway.section;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByUpStationId(Long upStationId);

    Optional<Section> findByDownStationId(Long DownStationId);
}
