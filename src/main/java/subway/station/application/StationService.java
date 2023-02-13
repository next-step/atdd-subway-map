package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.presentation.StationRequest;
import subway.station.presentation.StationResponse;
import subway.station.exception.StationNotFoundException;
import subway.station.infra.StationRepository;

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

    public StationResponse findStationById(Long id) {
        /*
        * StationResponse는 응용 계층에게 전달하기 위한 Dto인데 도메인 간에 사용하는 Dto를 따로 만드는 편이 좋을 것 같다.
        * 네이밍을 어떻게 해야하지..?
        * */
        Station station = stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
        return createStationResponse(station);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
