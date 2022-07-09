package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationCreateDto;
import nextstep.subway.applicaion.dto.StationDto;
import nextstep.subway.ui.dto.StationRequest;
import nextstep.subway.ui.dto.StationResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
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
    public StationDto saveStation(StationCreateDto stationCreateDto) {
        Station station = stationRepository.save(new Station(stationCreateDto.getName()));
        return createStationResponse(station);
    }

    public List<StationDto> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationDto createStationResponse(Station station) {
        return new StationDto(
                station.getId(),
                station.getName()
        );
    }
}
