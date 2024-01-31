package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByLineIdOrderById(Long lineId);

    void deleteAllByLineId(Long lineId);
}
