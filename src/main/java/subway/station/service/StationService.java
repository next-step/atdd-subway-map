package subway.station.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.service.request.StationRequest;
import subway.station.service.response.StationResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        final var station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.toResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteStation(final Long id) {
        stationRepository.deleteById(id);
    }
}
