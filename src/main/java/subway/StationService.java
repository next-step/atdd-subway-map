package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Station;
import subway.domain.StationRequest;
import subway.domain.StationResponse;
import subway.exception.NoStationException;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public StationResponse findStationById(Long id) {
        return stationRepository.findById(id)
                                .map(station -> new StationResponse(station.getId(), station.getName()))
                                .orElseThrow(() -> new NoStationException("지하철 역이 없습니다."));
    }

    public StationResponse findStationById(Long id) {
        return stationRepository.findById(id)
                                .map(station -> new StationResponse(station.getId(), station.getName()))
                                .orElseThrow(() -> new NoStationException("지하철 역이 없습니다."));
    }
}
