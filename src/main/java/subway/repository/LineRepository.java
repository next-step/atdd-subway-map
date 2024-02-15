package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import subway.domain.Line;
@Repository
public interface LineRepository extends JpaRepository<Line, Long> {
}
