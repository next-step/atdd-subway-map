package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station saveStation(StationRequest stationRequest) {
        return stationRepository.save(new Station(stationRequest.getName()));
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll().stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

}
