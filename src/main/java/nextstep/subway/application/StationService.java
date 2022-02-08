package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.StationRequest;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StationService {
    private static final String STATION_NAME_IS_ALREADY_REGISTERED = "이미 등록된 역 이름입니다.";
    public static final String GIVEN_STATION_ID_IS_NOT_REGISTERED = "등록되지 않은 역ID입니다.";
    private final StationRepository stationRepository;

    public StationResponse save(StationRequest stationRequest) {
        validate(stationRequest.getName());

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    private void validate(String stationName) {
        if (stationRepository.existsByName(stationName)) {
            throw new IllegalArgumentException(STATION_NAME_IS_ALREADY_REGISTERED);
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    public void deleteBy(Long id) {
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

    public Station findBy(Long id) {
        return stationRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(GIVEN_STATION_ID_IS_NOT_REGISTERED));
    }
}
