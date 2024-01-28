package subway.line;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.code.UseStatus;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    List<Line> findAllByUseStatus(UseStatus useStatus);

    Optional<Line> findByIdAndUseStatus(Long id, UseStatus useStatus);

}
