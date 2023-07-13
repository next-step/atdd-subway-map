package subway.subway.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.rds_module.entity.SubwayLineJpa;

import java.util.List;
import java.util.Optional;

public interface SubwayLineRepository extends JpaRepository<SubwayLineJpa, Long> {

    @Query("SELECT DISTINCT s FROM SubwayLineJpa s JOIN FETCH s.subwaySections")
    List<SubwayLineJpa> findAllWithSections();

    @Query("SELECT DISTINCT s FROM SubwayLineJpa s JOIN FETCH s.subwaySections " +
            "WHERE s.id = :id")
    Optional<SubwayLineJpa> findOneWithSectionsById(Long id);
}
