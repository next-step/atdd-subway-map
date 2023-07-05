package subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.line.domain.exception.NoSuchLineException;

public interface LineRepository extends JpaRepository<Line, Long> {

    default Line getById(final Long id) throws NoSuchLineException {
        return findById(id).orElseThrow(() -> NoSuchLineException.from(id));
    }
}
