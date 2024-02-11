package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.StationLine;

public interface StationSectionRepository extends JpaRepository<StationSection, Long> {
}
