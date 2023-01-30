package subway.line.infra;

import org.springframework.stereotype.Component;
import subway.line.domain.StationValidator;
import subway.station.domain.Station;
import subway.station.infra.JpaStationRepository;

import java.util.List;
import java.util.Set;

@Component
public class StationClient implements StationValidator {

    private final JpaStationRepository jpaStationRepository;

    public StationClient(JpaStationRepository jpaStationRepository) {
        this.jpaStationRepository = jpaStationRepository;
    }

    @Override
    public boolean existsStations(Long... ids) {
        final List<Station> stations = jpaStationRepository.findAllByIdIn(Set.of(ids));
        return stations.size() == ids.length;
    }
}
