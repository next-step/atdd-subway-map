package nextstep.subway.domain;

import nextstep.subway.infra.JpaLineRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineRepository extends JpaLineRepository {
    @Override
    List<Line> findAll();

    @Override
    Optional<Line> findById(Long aLong);

    @Override
    <S extends Line> S save(S entity);

    @Override
    void deleteById(Long aLong);
}
