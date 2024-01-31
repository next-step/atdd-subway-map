package subway.api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Section;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface SectionRepository extends JpaRepository<Section,Long> {
	void deleteByLine(Line line);
}
