package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationResponse saveStation(StationRequest stationRequest) {
        validateDuplicateStationName(stationRequest.getName());
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getCreatedDate(),
                station.getModifiedDate()
        );
    }

    private void validateDuplicateStationName(String name) {
        stationRepository.findByName(name).ifPresent(station -> {
            throw new BadRequestException("해당 역은 이미 존재합니다.");
        });
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 역입니다. stationId: " + id));
    }

}
