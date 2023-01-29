package subway.line;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.common.exception.line.LineNotFound;

public interface LineRepository extends JpaRepository<Line, Long> {
    default Line getLine(Long id) {
        return findById(id)
                .orElseThrow(LineNotFound::new);
    }

}
