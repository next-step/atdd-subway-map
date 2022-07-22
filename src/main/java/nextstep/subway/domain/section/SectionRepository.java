package nextstep.subway.domain.section;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    boolean existsByUpStation_Id(Long upStationId);
    boolean existsByDownStation_Id(Long downStationId);

}