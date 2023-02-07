package subway.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByLineOrderByUpStation(Line line);

    Optional<Section> findByStation(Station station);

    Optional<Section> findByStationAndLine(Station station, Line line);

    @Query("SELECT s FROM Section s where s.station IN :stations")
    List<Section> findAllByStation(@Param("stations") List<Station> stations);

    void deleteAllByStation(Station station);
}
