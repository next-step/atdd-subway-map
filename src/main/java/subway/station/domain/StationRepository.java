package subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.station.domain.exception.NoSuchStationException;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station getById(final Long id) throws NoSuchStationException {
        return findById(id).orElseThrow(() -> NoSuchStationException.from(id));
    }
}
