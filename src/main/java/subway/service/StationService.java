package subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.service.dto.request.StationRequest;
import subway.service.dto.response.StationResponse;

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
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
