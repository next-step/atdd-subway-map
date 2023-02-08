package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.StationNotFoundException;
import subway.domain.StationRepository;

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
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    /**
     * Station 엔티티를 반환하는 메서드
     *
     * @param stationId
     * @return Station
     * @throws StationNotFoundException 존재하지 않는 id로 조회시
     */
    public Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }
}
