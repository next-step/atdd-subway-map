package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.Line;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> { }
