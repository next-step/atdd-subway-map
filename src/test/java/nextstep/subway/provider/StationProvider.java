package nextstep.subway.provider;

import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StationProvider {

    @Autowired
    private StationRepository stationRepository;

    public Long createStation(String stationName) {
        final Station savedStation = stationRepository.save(new Station(stationName));
        return savedStation.getId();
    }

    public void createStations(List<String> stationNames) {
        final List<Station> stations = new ArrayList<>();
        for (String stationName : stationNames) {
            stations.add(new Station(stationName));
        }
        stationRepository.saveAll(stations);
    }
}
