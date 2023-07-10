package subway.model.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station save(Station newStation) {
        return stationRepository.save(newStation);
    }

    public Station findById(Long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));
    }

    @Transactional
    public void deleteById(Long stationId) {
        stationRepository.deleteById(stationId);
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }
}
