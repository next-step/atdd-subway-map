package subway.domain.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.station.Station;
import subway.domain.repository.StationRepository;

@Service
@RequiredArgsConstructor
public class StationCommander {
    private final StationRepository stationRepository;

    @Transactional
    public Long createStation(String name) {
        Station station = stationRepository.save(new Station(name));
        return station.getId();
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
