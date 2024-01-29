package subway.line;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    Optional<Line> findById(Long id);

//    List<Line> findAllByUseStatus(UseStatus useStatus);

//    Optional<Line> findByIdAndUseStatus(Long id, UseStatus useStatus);

}
