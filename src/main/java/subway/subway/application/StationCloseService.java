package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.in.StationCloseUsecase;

@Service
@Transactional
class StationCloseService implements StationCloseUsecase {
    private final StationRepository stationRepository;

    public StationCloseService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

}
