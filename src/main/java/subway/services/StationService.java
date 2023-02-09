package subway.services;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import subway.dto.request.StationRequest;
import subway.models.Station;
import subway.repositories.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public Station saveStation(StationRequest stationRequest) {
        return stationRepository.save(new Station(stationRequest.getName()));
    }

    public Station findById(Long id) {
        return stationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
