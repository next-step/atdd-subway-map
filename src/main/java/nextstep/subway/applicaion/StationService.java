package nextstep.subway.applicaion;

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

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

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
       if(stationRepository.findByName(name) != null) {
           throw new BadRequestException("중복된 역 이름입니다.");
       }
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
                .orElseThrow(() -> new BadRequestException("존재하지 않는 역입니다."));
    }

    public Station findById(Long id, String exceptionMessage) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(exceptionMessage));
    }

}
