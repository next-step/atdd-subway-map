package subway.domain;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

    default Line getById(final Long id) {
        return findById(id).orElseThrow(NoSuchElementException::new);
    }
}
