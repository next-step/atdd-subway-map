package nextstep.subway.domain.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Set<Section> findAllByLine_LineId(Long lineId);

}