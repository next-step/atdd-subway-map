package subway;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {
    void deleteByLineIdAndStationId(Long lineId, Long stationId);
    List<Station> findAllByLineId(Long lineId);

}
