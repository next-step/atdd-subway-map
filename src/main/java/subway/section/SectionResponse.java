package subway.section;

import java.util.List;

import subway.station.Station;

public class SectionResponse {
    private final Station upStation;
    private final Station downStation;

    public SectionResponse(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }
}
