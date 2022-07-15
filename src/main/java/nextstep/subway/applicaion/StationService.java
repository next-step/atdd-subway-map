package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationCreateDto;
import nextstep.subway.applicaion.dto.StationDto;
import nextstep.subway.domain.Station;
import nextstep.subway.infra.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;


    @Transactional
    public StationDto saveStation(StationCreateDto stationCreateDto) {
        Station station = stationRepository.save(Station.builder().name(stationCreateDto.getName()).build());
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
