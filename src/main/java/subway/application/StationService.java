package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.ui.dto.StationRequest;
import subway.ui.dto.StationResponse;
import subway.domain.Station;
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
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
