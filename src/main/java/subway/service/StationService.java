package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.request.StationRequest;
import subway.controller.response.StationResponse;
import subway.exception.SubwayRuntimeException;
import subway.exception.message.SubwayErrorCode;
import subway.repository.StationRepository;
import subway.repository.entity.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    @Transactional
    public StationResponse create(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream().map(this::createStationResponse).collect(Collectors.toList());
    }

    public Station find(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new SubwayRuntimeException(SubwayErrorCode.NOT_FOUND_STATION));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.from(station);
    }
}
