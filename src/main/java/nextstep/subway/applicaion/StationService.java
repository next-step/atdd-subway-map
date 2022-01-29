package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.service.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;
    private final Validator<Station> stationValidator;

    public StationService(final StationRepository stationRepository, final Validator<Station> stationValidator) {
        this.stationRepository = stationRepository;
        this.stationValidator = stationValidator;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName(), stationValidator));
        return createStationResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(final Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getCreatedDate(),
                station.getModifiedDate()
        );
    }
}
