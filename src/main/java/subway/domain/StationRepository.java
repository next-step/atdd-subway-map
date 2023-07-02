package subway.domain;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station getById(final Long id) {
        return findById(id).orElseThrow(NoSuchElementException::new);
    }
}
