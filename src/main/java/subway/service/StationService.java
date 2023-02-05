package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.station.Station;
import subway.dto.station.CreateStationRequest;
import subway.dto.station.CreateStationResponse;
import subway.dto.station.ReadStationListResponse;
import subway.dto.station.ReadStationResponse;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public CreateStationResponse createStation(CreateStationRequest createStationRequest) {
        Station station = stationRepository.save(createStationRequest.convertDtoToEntity());
        return new CreateStationResponse(station);
    }

    @Transactional(readOnly = true)
    public List<ReadStationListResponse> readStationList() {
        return stationRepository.findAll()
                .stream()
                .map(ReadStationListResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReadStationResponse readStation(Long id) {
        return stationRepository.findById(id)
                .map(ReadStationResponse::new)
                .orElse(null);

    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

}
