package nextstep.subway.infra;

import nextstep.subway.domain.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStationRepository extends JpaRepository<Station, Long> {
}
