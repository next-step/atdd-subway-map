package subway.station;

import org.springframework.stereotype.Service;
import subway.exception.SubwayNotFoundException;
import subway.line.dto.LineDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationQuery {
    private StationRepository stationRepository;

    public StationQuery(StationRepository stationRepository) { this.stationRepository = stationRepository; }

    public Stations getStations(List<Long> stationIds) {
        return new Stations(
                stationRepository.findByIdIn(stationIds).stream()
                        .collect(
                                Collectors.toMap(
                                        Station::getId,
                                        station -> station
                                )
                        )
        );
    }

    public Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                        .orElseThrow(() -> new SubwayNotFoundException("Station not found with id, " + stationId));
    }
}
