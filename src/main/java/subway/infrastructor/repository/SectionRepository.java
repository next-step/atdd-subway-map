package subway.infrastructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SectionRepository extends JpaRepository<SectionJpaEntity, Long> {

    List<SectionJpaEntity> findByLineId(LinePk lineId);

    Optional<SectionJpaEntity> findByIdAndLineId(Long sectionId, LinePk lienId);

}
