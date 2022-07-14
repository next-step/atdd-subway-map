package nextstep.subway.applicaion;

import java.util.List;
import java.util.Optional;
import nextstep.subway.domain.StationSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationSectionRepository extends JpaRepository<StationSection, Long> {

    List<StationSection> findAllOrderByIdDesc();
}
