package nextstep.subway.applicaion.section.domain;

import nextstep.subway.applicaion.line.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLineId(Long lineId);
}
