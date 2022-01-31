package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    void deleteByLineIdAndDownStationId(Long lineId, Long downStationId);
}
