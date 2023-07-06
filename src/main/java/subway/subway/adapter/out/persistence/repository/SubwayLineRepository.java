package subway.subway.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.rds_module.entity.SubwayLineJpa;

import java.util.List;

public interface SubwayLineRepository extends JpaRepository<SubwayLineJpa, Long> {

    @Query("SELECT DISTINCT s FROM SubwayLineJpa s JOIN FETCH s.subwaySections")
    List<SubwayLineJpa> findAllWithSections();
}
