package subway.line.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, SectionId> {
    List<Section> findByLineId(Long id);
}
