package nextstep.subway.section.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLineIdOrderById(Long lineId);

    boolean existsByUpStationIdOrDownStationId(Long upStationId, Long downStationId);

    void deleteByDownStationIdAndLineId(Long downStationId, Long lineId);

    void deleteByLineId(Long lineId);
}
