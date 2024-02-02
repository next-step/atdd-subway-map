package subway.line;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {


    @EntityGraph(attributePaths = {"upStation", "downStation"})
    @Query("select line from Line line")
    List<Line> findAllWithStations();

    @Query("select line from Line line where line.id = :id")
    Optional<Line> findByIdWithStations(
        @Param("id") Long id
    );
}