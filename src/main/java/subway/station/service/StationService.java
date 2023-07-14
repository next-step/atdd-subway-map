package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.dto.StationDto;
import subway.station.entity.Station;
import subway.station.entity.StationRepository;

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
    public StationDto saveStation(StationDto stationDto) {
        if (stationRepository.findByName(stationDto.getName()).isPresent()) {
            throw new IllegalArgumentException(String.format("이미 존재하는 역 이름입니다. 역 이름:%s", stationDto.getName()));
        }

        Station station = stationRepository.save(new Station(stationDto.getName()));
        return StationDto.from(station);
    }

    public List<StationDto> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
