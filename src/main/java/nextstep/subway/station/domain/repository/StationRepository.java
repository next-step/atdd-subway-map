package nextstep.subway.station.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.station.domain.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
    boolean existsByName(String name);
}
