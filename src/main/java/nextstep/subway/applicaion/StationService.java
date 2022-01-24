package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateRegistrationRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest request) throws DuplicateRegistrationRequestException {
        Station findStation = stationRepository.findByName(request.getName());
        if (ObjectUtils.isEmpty(findStation)) {
            Station station = stationRepository.save(new Station(request.getName()));
            return StationResponse.createStationResponse(station);
        }

        throw new DuplicateRegistrationRequestException("이미 등록된 역입니다. 역 이름 = " + request.getName());
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::createStationResponse)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
