package nextstep.subway.infra;

import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStationRepository extends JpaRepository<Station, Long>, StationRepository {
    @Override
    List<Station> findAll();

    @Override
    Optional<Station> findById(Long aLong);

    @Override
    <S extends Station> S save(S entity);

    @Override
    void deleteById(Long aLong);
}
