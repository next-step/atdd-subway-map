package subway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Transactional
    @Modifying
    @Query(
            value = "TRUNCATE TABLE Station RESTART IDENTITY",
            nativeQuery = true
    )
    void truncateTableStation();
}
