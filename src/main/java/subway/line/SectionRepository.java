package subway.line;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
	List<Section> findAllByLineId(Long lineId);
}
