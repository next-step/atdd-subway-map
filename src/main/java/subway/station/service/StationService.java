package subway.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;
import subway.station.model.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        stationRepository.findByName(stationRequest.getName())
                .ifPresent(e -> {throw new IllegalArgumentException("이미 역이 있습니다.");});
        Station station = Station.builder()
                .name(stationRequest.getName())
                .build();
        return StationResponse.from(stationRepository.save(station));
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findEntityById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 역 입니다"));
    }
}
