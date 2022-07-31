package nextstep.subway.applicaion.station;

import nextstep.subway.applicaion.common.StationNotFoundException;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.domain.StationRepository;
import nextstep.subway.applicaion.station.dto.StationRequest;
import nextstep.subway.applicaion.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public Station getStationThrowExceptionIfNotExists(Long id) {
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }

    public StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
