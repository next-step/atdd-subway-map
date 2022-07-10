package nextstep.subway.domain;

import nextstep.subway.infra.JpaStationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaStationRepository {

    @Override
    List<Station> findAll();

    @Override
    Optional<Station> findById(Long aLong);

    @Override
    <S extends Station> S save(S entity);

    @Override
    void deleteById(Long aLong);
}
