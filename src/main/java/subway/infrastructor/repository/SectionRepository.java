package subway.infrastructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SectionRepository extends JpaRepository<SectionJpaEntity, Long> {

    List<SectionJpaEntity> findByLineId(LinePk lineId);

}
