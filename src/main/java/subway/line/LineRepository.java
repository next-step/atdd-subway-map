package subway.line;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.common.exception.line.LineNotFoundExceptionException;

public interface LineRepository extends JpaRepository<Line, Long> {
    default Line getLine(Long id) {
        return findById(id)
                .orElseThrow(LineNotFoundExceptionException::new);
    }

}
