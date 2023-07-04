package subway.station.domain;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station getById(final Long id) throws NoSuchElementException {
        return findById(id).orElseThrow(() -> new NoSuchElementException("지하철 역을 찾을 수 없습니다 : id=" + id));
    }
}
