package nextstep.subway.applicaion;

import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationVerificationService {
    private final StationRepository stationRepository;

    public StationVerificationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public boolean isExistStationByStationName(String stationName) {
        return stationRepository.findByName(stationName).isPresent();
    }
}
