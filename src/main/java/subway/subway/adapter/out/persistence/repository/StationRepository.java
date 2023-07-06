package subway.subway.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.rds_module.entity.StationJpa;

import java.util.List;

public interface StationRepository extends JpaRepository<StationJpa, Long> {

    List<StationJpa> findAllByIdIn(List<Long> stationIds);
}