package nextstep.subway.applicaion.station;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateCreationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationModifyService {
    private final StationRepository stationRepository;

    public StationModifyService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        stationRepository
                .findByName(stationRequest.getName())
                .ifPresent(
                        station -> {
                            throw new DuplicateCreationException();
                        });

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
