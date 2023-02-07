package subway.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByLine(Line line);

    Optional<Section> findByLineAndDownStation(Line line, Station downStation);

    List<Section> findByUpStation(Station upStation);
}
