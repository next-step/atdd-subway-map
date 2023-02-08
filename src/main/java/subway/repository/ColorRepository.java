package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.color.Color;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByName(String name);
}
