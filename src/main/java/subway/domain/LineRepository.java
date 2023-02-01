package subway.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM LINE; ALTER TABLE LINE ALTER COLUMN id RESTART WITH 1;",
            nativeQuery = true
    )
    void deleteAllAndRestartId();

    @Override
    @Query("select l from Line l join fetch l.stations.stations")
    List<Line> findAll();
}
