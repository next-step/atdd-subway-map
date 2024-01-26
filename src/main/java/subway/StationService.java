package subway;

import java.util.Optional;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

    @Transactional
    public StationResponse updateStation(
        Long id,
        StationRequest stationRequest
    ) throws NotFoundException {
        final Station foundStation = stationRepository.findById(id).orElse(null);
        if(foundStation == null) {
            throw new NotFoundException();
        }

        System.out.println(foundStation);

        foundStation.setName(stationRequest.getName());

        return createStationResponse(foundStation);
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

    public StationResponse findStation(Long id) {
        final Station foundStation = stationRepository.findById(id).orElse(null);

        if(foundStation == null) {
            return null;
        }

        return createStationResponse(foundStation);
    }
}
