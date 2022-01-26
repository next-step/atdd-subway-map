package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateStoreException;
import nextstep.subway.exception.NotFoundException;

@Service
@Transactional
public class StationService {
    private final String NOT_EXISTS_STATION = "해당 지하철역에 대한 정보가 없습니다.";
    private final String DUPLICATE_STATION_NAME = "지하철역 이름이 중복입니다.";

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationRepository.existsByName(stationRequest.getName())) {
            throw new DuplicateStoreException(DUPLICATE_STATION_NAME);
        }

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StationResponse findStationById(Long id) {
        Station station = stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_EXISTS_STATION));

        return StationResponse.of(station);
    }

    public void updateStationById(Long id, StationRequest request) {
        Station station = stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_EXISTS_STATION));

        station.update(request.getName());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
