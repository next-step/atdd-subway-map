package nextstep.subway.section.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByDownStationId(Long downStationId);
    @Transactional
    void deleteByDownStationId(Long downStationId);
    long countByLine_id(Long lineId);
    List<Section> findByLine_id(Long lineId);

    Optional<Section> findByLine_idAndUpStation_status(Long lineId, StationStatus start);

    Optional<Section> findByLine_idAndUpStation(Long lineId, Station upStation);
}
