package subway.line.station;

import subway.line.Line;
import subway.line.LineDataSet;
import subway.station.Station;

public class LineStationDataSet {

    public static LineStation testData() {
        return new LineStation(LineDataSet.testData(), StationDataSet.testData());
    }

    public static LineStation testData(Line line, Station station) {
        return new LineStation(line, station);
    }
}
