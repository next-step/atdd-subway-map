package subway.line;

import org.springframework.stereotype.Service;
import subway.station.Station;

import java.util.List;

@Service
public class SectionService {

    Boolean isAppendable(final Line line, final SectionRequest sectionRequest) {
        List<Station> stations = line.getStations();

        if(stations.isEmpty())
            return Boolean.FALSE;

        Station lineDownStation = stations.get(stations.size() - 1);
        Long sectionUpStationId = sectionRequest.getUpStationId();

        if(!lineDownStation.getId().equals(sectionUpStationId))
            return Boolean.FALSE;

        if(isAppended(stations, sectionRequest.getDownStationId()))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    private Boolean isAppended(final List<Station> stations, final Long newStationId) {
        return stations.stream()
                .anyMatch((s) -> s.getId().equals(newStationId));
    }
}
