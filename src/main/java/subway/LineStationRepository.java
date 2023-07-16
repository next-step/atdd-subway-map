package subway;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {

    LineStation findFirstByLineIdOrderBySequence(Long id);
    LineStation findFirstByLineIdOrderBySequenceDesc(Long id);
    List<LineStation> findAllByLineIdOrderBySequenceDesc(Long id);
    void deleteByLineIdAndStationId(Long lineId, Long stationId);

}
