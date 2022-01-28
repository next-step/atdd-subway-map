package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByDownStationId(Long stationId);

    List<Section> findByLineOrderByIdAsc(Line line);
}
