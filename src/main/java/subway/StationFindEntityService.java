package subway;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class StationFindEntityService {

    private final StationRepository stationRepository;

    public StationFindEntityService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station getById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NoSuchElementException::new);
    }

}
