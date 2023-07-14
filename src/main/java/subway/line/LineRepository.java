package subway.line;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.line.domain.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}