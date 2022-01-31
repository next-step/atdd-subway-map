package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.applicaion.exception.AlreadyRegisteredStationException;
import nextstep.subway.domain.PairedStations;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;
    private StationVerificationService stationVerificationService;

    public StationService(StationRepository stationRepository, StationVerificationService stationVerificationService) {
        this.stationRepository = stationRepository;
        this.stationVerificationService = stationVerificationService;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        String stationName = stationRequest.getName();
        if (stationVerificationService.isExistStationByStationName(stationName)) {
            throw new AlreadyRegisteredStationException(stationName);
        }

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getCreatedDate(),
                station.getModifiedDate()
        );
    }

    public PairedStations createPairedStations(Long upStationId, Long downStationId) {
        return new PairedStations(findStationById(upStationId), findStationById(downStationId));
    }
}
