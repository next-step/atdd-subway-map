package nextstep.subway.section.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLineId(Long lineId);

    void deleteByDownStationIdAndLineId(Long downStationId, Long lineId);
}
