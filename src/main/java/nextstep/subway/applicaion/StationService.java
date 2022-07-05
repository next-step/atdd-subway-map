package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
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

    private static Station apply(StationRequest sr) {
        return new Station(sr.getName());
    }

    @Transactional
    public List<StationResponse> saveStation(List<StationRequest> stationRequest) {
        List<Station> stations = stationRepository.saveAll(stationRequest.stream().map(StationService::apply).collect(Collectors.toList()));
        return createStationResponse(stations);
    }

    public List<StationResponse> findAllStations() {
        return createStationResponse(stationRepository.findAll());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private List<StationResponse> createStationResponse(List<Station> stations) {
        return stations.stream().map(station -> new StationResponse(station.getId(), station.getName())).collect(Collectors.toList());
    }
}
