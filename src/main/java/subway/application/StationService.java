package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.StationAddDto;
import subway.application.dto.StationDto;
import subway.domain.Station;
import subway.domain.StationRepository;

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
    public StationDto saveStation(StationAddDto stationAddDto) {
        Station station = stationRepository.save(new Station(stationAddDto.getName()));
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
