package subway.station.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.repository.entity.Station;
import subway.station.repository.StationRepository;
import subway.station.web.StationRequest;
import subway.station.web.StationResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return toStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::toStationResponse)
                .collect(Collectors.toList());
    }

    public Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new NoSuchElementException());
    }

    public StationResponse toStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

}
