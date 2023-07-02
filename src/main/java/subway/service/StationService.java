package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.StationNotFoundException;
import subway.controller.resonse.StationResponse;
import subway.domain.Station;
import subway.repository.StationRepository;
import subway.service.command.StationCreateCommand;

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
    public StationResponse saveStation(StationCreateCommand createCommand) {
        Station station = stationRepository.save(new Station(createCommand.getName()));
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

    public StationResponse findStation(Long id) {
        Station station = requireGetById(id);
        return this.createStationResponse(station);
    }

    private Station requireGetById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
