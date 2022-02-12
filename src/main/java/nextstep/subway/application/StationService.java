package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.StationRequest;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.exception.StationException.GIVEN_STATION_ID_IS_NOT_REGISTERED;
import static nextstep.subway.exception.StationException.STATION_NAME_IS_ALREADY_REGISTERED;

@Service
@Transactional
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    public StationResponse save(StationRequest stationRequest) {
        validate(stationRequest.getName());

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    private void validate(String stationName) {
        if (stationRepository.existsByName(stationName)) {
            throw new BadRequestException(STATION_NAME_IS_ALREADY_REGISTERED);
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();
        return StationResponse.stationResponses(stations);
    }

    public void deleteBy(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findBy(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(GIVEN_STATION_ID_IS_NOT_REGISTERED));
    }
}
