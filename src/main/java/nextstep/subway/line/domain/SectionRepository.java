package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findOneById(Long id);

    List<Section> findAllByLineId(Long lineId);
}
