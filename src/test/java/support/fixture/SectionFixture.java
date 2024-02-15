package support.fixture;

import subway.station.StationResponse;
import support.step.StationSteps;

public class SectionFixture {

    public Long upStationId;
    public Long downStationId;
    public Long distance;

    private SectionFixture(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }


}


