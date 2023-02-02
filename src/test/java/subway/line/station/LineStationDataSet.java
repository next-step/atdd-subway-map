package subway.line.station;

import subway.line.Line;
import subway.station.Station;

public class LineStationDataSet {

    public static LineStation testData(Line line, Station station) {
        return new LineStation(line, station);
    }
}
