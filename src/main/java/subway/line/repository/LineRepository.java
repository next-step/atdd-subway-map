package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.repository.entity.LineEntity;

public interface LineRepository extends JpaRepository<LineEntity, Long> {
}