package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationDto;
import subway.jpa.Station;
import subway.jpa.StationRepository;

import java.util.List;
import java.util.Optional;
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
        Optional<Station> name = stationRepository.findByName(stationDto.getName());
        if (name.isPresent()) {
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
