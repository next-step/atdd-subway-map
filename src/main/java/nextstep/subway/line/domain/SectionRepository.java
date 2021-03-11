package nextstep.subway.line.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findFirstByDownStation(Station station);
    Section findFirstByUpStation(Station station);
    boolean existsByUpStation(Station station);
    boolean existsByDownStation(Station station);
}
