package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationCreateDto;
import nextstep.subway.applicaion.dto.StationDto;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationApiService {
    private final StationService stationService;


    @Transactional
    public StationDto saveStation(StationCreateDto stationCreateDto) {
        Station station = stationService.save(new Station(stationCreateDto.getName()));
        return createStationResponse(station);
    }

    public List<StationDto> findAllStations() {
        return stationService.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationService.deleteById(id);
    }

    private StationDto createStationResponse(Station station) {
        return new StationDto(
                station.getId(),
                station.getName()
        );
    }
}
