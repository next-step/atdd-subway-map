package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.request.StationRequest;
import nextstep.subway.applicaion.dto.response.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateRegistrationRequestException;
import nextstep.subway.exception.NotFoundRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    public static final String STATION_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE = "이미 등록된 역입니다. 역 이름 = %s";
    public static final String STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE = "존재하지 않는 역입니다. id = %s";

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest request) throws DuplicateRegistrationRequestException {
        boolean existsStation = stationRepository.existsByName(request.getName());
        if (existsStation) {
            throw new DuplicateRegistrationRequestException(
                    String.format(STATION_DUPLICATE_REGISTRATION_EXCEPTION_MESSAGE, request.getName())
            );
        }

        Station station = stationRepository.save(Station.createStation(request.getName()));
        return StationResponse.createStationResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::createStationResponse)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException(
                        String.format(STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, id))
                );

        stationRepository.delete(station);
    }

    public Station findStationById(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new NotFoundRequestException(
                        String.format(StationService.STATION_NOT_FOUND_REQUEST_EXCEPTION_MESSAGE, upStationId))
                );
    }
}
