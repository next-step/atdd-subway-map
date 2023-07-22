package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.LineSection;

public interface LineSectionRepository extends JpaRepository<LineSection, Long> {

}
