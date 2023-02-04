package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.dto.request.StationRequest;
import subway.station.domain.Station;
import subway.station.domain.StationCommandRepository;
import subway.station.domain.StationQueryRepository;
import subway.station.exception.StationNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationQueryRepository stationQueryRepository;
    private final StationCommandRepository stationCommandRepository;

    public StationService(final StationQueryRepository stationQueryRepository,
                          final StationCommandRepository stationCommandRepository) {
        this.stationQueryRepository = stationQueryRepository;
        this.stationCommandRepository = stationCommandRepository;
    }

    public List<Station> findAllStations() {
        return stationQueryRepository.findAll();
    }

    public Station findStationById(final Long stationId) {
        return stationQueryRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    @Transactional
    public Long saveStation(final StationRequest stationRequest) {
        Station station = stationCommandRepository.save(stationRequest.toEntity());

        return station.getId();
    }

    @Transactional
    public void deleteStationById(final Long stationId) {
        stationCommandRepository.deleteById(stationId);
    }
}
