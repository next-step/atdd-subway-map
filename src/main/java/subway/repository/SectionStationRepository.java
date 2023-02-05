package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.sectionstation.SectionStation;

public interface SectionStationRepository extends JpaRepository<SectionStation, Long> {
}
