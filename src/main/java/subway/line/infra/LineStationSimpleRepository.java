package subway.line.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.LineStationConnection;

public interface LineStationSimpleRepository  extends LineStationRepository, JpaRepository<LineStationConnection, Long> {
}
