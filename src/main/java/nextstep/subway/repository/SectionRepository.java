package nextstep.subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
