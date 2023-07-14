package subway.line;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineConverter {
    public LineResponse convert(Line line, List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}
