package subway.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.api.dto.StationRequest;
import subway.api.dto.StationResponse;
import subway.domain.entity.Station;
import subway.domain.repository.StationRepository;
import subway.global.error.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static subway.global.error.exception.ErrorCode.STATION_NOT_EXISTS;

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

    StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    public Station findById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(()-> new EntityNotFoundException(STATION_NOT_EXISTS));
    }
}
