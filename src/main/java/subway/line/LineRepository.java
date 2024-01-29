package subway.line;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Override
    <S extends Line> S save(S entity);

    @Override
    Optional<Line> findById(Long aLong);
}
