package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Line;
import subway.entity.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
	List<Section> findByLine(Line line);
}
