package subway.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.line.Line;
import subway.domain.exception.NotFoundLineException;

public interface LineRepository extends JpaRepository<Line, Long> {
    @NonNull
    default Line findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundLineException(id));
    }
}
