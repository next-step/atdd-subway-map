package subway.station.application;

import org.springframework.stereotype.Component;
import subway.exception.StationNotFoundException;
import subway.station.application.dto.Stations;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationQuery {
    private StationRepository stationRepository;

    public StationQuery(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    public Stations findAllByIdIn(List<Long> ids) {
        return new Stations(stationRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(
                        Station::getId,
                        station -> station
                )));
    }
}
