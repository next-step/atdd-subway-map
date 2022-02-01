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
import nextstep.subway.ui.ExceptionMessage;

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationRepository.existsByName(stationRequest.getName())) {
            throw new DuplicateStoreException(ExceptionMessage.DUPLICATE_STATION_NAME.getMessage());
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
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_STATION.getMessage()));

        return StationResponse.of(station);
    }

    public void updateStationById(Long id, StationRequest request) {
        Station station = stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_STATION.getMessage()));

        station.update(request.getName());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
