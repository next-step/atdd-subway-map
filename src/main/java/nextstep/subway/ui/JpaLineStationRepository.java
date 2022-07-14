package nextstep.subway.ui;

import nextstep.subway.domain.line.LineStationRepository;
import nextstep.subway.domain.line.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaLineStationRepository extends LineStationRepository, JpaRepository<Section, Long> {

    @Override
    List<Section> findAll();

    @Override
    Optional<Section> findById(Long aLong);

    @Override
    <S extends Section> S save(S entity);

    @Override
    void deleteById(Long aLong);
}
