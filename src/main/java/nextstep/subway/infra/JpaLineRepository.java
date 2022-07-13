package nextstep.subway.infra;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLineRepository extends JpaRepository<Line, Long>, LineRepository {
    @Override
    List<Line> findAll();

    @Override
    Optional<Line> findById(Long aLong);

    @Override
    <S extends Line> S save(S entity);

    @Override
    void deleteById(Long aLong);
}
