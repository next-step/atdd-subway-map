package subway.domain.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.global.error.ErrorCode;
import subway.global.error.exception.NotFoundException;
import subway.infrastructure.station.StationRepository;
import subway.presentation.station.dto.request.StationRequest;
import subway.presentation.station.dto.response.StationResponse;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

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

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    @Transactional(readOnly = true)
    public Station getStation(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_STATION));
    }
}
