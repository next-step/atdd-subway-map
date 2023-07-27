package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Stations;
import subway.station.repository.StationRepository;
import subway.station.domain.Station;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private static final String STATION_DOES_NOT_EXIST = "존재하지 않는 역입니다.";
    private static final String ALREADY_EXIST = "이미 존재하는 역 이름입니다.";

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        stationRepository.findByName(stationRequest.getName())
                .ifPresent(station -> {
                    throw new IllegalArgumentException(ALREADY_EXIST);
                });
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
    public Stations getStations(Long upStationId, Long downStationId) {
        return new Stations(
                getStation(upStationId),
                getStation(downStationId)
        );
    }

    @Transactional(readOnly = true)
    public Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException(STATION_DOES_NOT_EXIST));
    }

}
