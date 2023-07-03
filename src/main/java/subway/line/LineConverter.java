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
        if (line == null)
            return null;
        LineResponse lineResponse = new LineResponse();
        lineResponse.setId(line.getId());
        lineResponse.setName(line.getName());
        lineResponse.setColor(line.getColor());
        List<StationResponse> stationResponses = stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        lineResponse.setStations(stationResponses);
        return lineResponse;
    }
}
