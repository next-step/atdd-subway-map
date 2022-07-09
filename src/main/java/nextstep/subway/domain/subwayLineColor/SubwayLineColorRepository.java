package nextstep.subway.domain.subwayLineColor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubwayLineColorRepository extends JpaRepository<SubwayLineColor, Long> {

    Optional<SubwayLineColor> findByCode(String code);
}
