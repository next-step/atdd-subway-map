package subway.application.util;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Station;

@Component
public class Finder {

    public Station findStationById(final List<Station> stations, final Long stationId) {
        return stations.stream()
                .filter(station -> station.getId() == stationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("노선 지하철 정보가 올바르지 않습니다."));
    }
}
