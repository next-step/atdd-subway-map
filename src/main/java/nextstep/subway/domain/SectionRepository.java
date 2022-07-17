package nextstep.subway.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

  List<Section> findByLineOrderByIdAsc(Line line);

  void deleteByLine(Line line);
}
