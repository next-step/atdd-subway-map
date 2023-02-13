package subway.station;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.common.exception.station.StationNotFoundExceptionException;

public interface StationRepository extends JpaRepository<Station, Long> {
    default Station getStation(final Long id){
        return findById(id)
                .orElseThrow(StationNotFoundExceptionException::new);
    }

}