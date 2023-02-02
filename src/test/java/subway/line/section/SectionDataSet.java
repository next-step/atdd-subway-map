package subway.line.section;

import subway.section.Section;
import subway.station.Station;

public class SectionDataSet {

    public static Section testData(Station downStation, Station upStation, int distance) {
        return new Section(downStation, upStation, distance);
    }

}
